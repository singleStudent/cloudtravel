package com.cloudtravel.consumer.db;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = MainDataSourceConfig.BASE_PACKAGE , sqlSessionFactoryRef = "sqlSessionFactory")
public class MainDataSourceConfig {

    @Value("${mybatis.mapperLocations}")
    public String MAPPER_LOCATIONS;

    /** dao层接口所在的包 */
    public static final String BASE_PACKAGE = "com.cloudtravel.consumer.dao";

    @Value("${seata.application-id}")
    public String SEATA_APPLICATION_ID;

    @Value("${seata.tx-service-group}")
    public String TX_SERVICE_GROUP;

    @Bean("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    //for seata
    @Bean("dataSourceProxy")
    public DataSourceProxy dataSourceProxy(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceProxy") DataSourceProxy dataSourceProxy)throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean("globalTransactionScanner")
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner(SEATA_APPLICATION_ID , TX_SERVICE_GROUP);
    }

}
