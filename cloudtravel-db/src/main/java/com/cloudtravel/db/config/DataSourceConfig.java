package com.cloudtravel.db.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@MapperScan(basePackages = "com.cloudtravel.db.dao" , sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    public static final ConcurrentHashMap<Object , Object> dataSourceMap = new ConcurrentHashMap<>();

    @Autowired
    private DataSourceConfigBase dataSourceConfigBase;

    @Value("${mybatis.mapper-locations}")
    private String MAPPER_LOCATIONS;

    @Bean("cloudtravel_consumer1")
    @ConfigurationProperties(prefix = "datasource.cloudtravel-consumer1")
    public DataSource getCloudTravelDb1() throws Exception{
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setInitialSize(dataSourceConfigBase.getInitialSize());
        druidDataSource.setMinIdle(dataSourceConfigBase.getMinIdle());
        druidDataSource.setMaxActive(dataSourceConfigBase.getMaxActive());
        druidDataSource.setMaxWait(dataSourceConfigBase.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(dataSourceConfigBase.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setTestWhileIdle(dataSourceConfigBase.getTestWhileIdle());
        druidDataSource.setTestOnBorrow(dataSourceConfigBase.getTestOnBorrow());
        druidDataSource.setTestOnReturn(dataSourceConfigBase.getTestOnReturn());
        druidDataSource.setPoolPreparedStatements(dataSourceConfigBase.getPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceConfigBase.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setFilters(dataSourceConfigBase.getFilter());
        return druidDataSource;
    }

    @Bean("cloudtravel_consumer2")
    @ConfigurationProperties(prefix = "datasource.cloudtravel-consumer2")
    public DataSource getCloudTravelDb2() throws Exception{
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setInitialSize(dataSourceConfigBase.getInitialSize());
        druidDataSource.setMinIdle(dataSourceConfigBase.getMinIdle());
        druidDataSource.setMaxActive(dataSourceConfigBase.getMaxActive());
        druidDataSource.setMaxWait(dataSourceConfigBase.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(dataSourceConfigBase.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setTestWhileIdle(dataSourceConfigBase.getTestWhileIdle());
        druidDataSource.setTestOnBorrow(dataSourceConfigBase.getTestOnBorrow());
        druidDataSource.setTestOnReturn(dataSourceConfigBase.getTestOnReturn());
        druidDataSource.setPoolPreparedStatements(dataSourceConfigBase.getPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceConfigBase.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setFilters(dataSourceConfigBase.getFilter());
        return druidDataSource;
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSourceInit(@Qualifier("cloudtravel_consumer1")DataSource dataSource1,
                                            @Qualifier("cloudtravel_consumer2")DataSource dataSource2) {
        dataSourceMap.put(DataSourceEnums.CLOUDTRAVEL_CONSUMER1 , dataSource1);
        dataSourceMap.put(DataSourceEnums.CLOUDTRAVEL_CONSUMER2 , dataSource2);
        DynamicDataSource dataSource = new DynamicDataSource();
        //设置当前数据源映射map
        dataSource.setTargetDataSources(dataSourceMap);
        //设置默认数据源
        dataSource.setDefaultTargetDataSource(dataSource1);
        return dataSource;
    }

    @Bean("dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager (@Qualifier("dynamicDataSource") DynamicDataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dataSource)throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS));
//        sqlSessionFactoryBean.setTransactionFactory(new DynamicDataSourceTransactionFactory());
        //引入数据源路由策略类
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new SqlDbRoutingInterceptor()});
        return sqlSessionFactoryBean.getObject();
    }
}
