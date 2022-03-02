package com.cloudtravel.shardingsphere.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.cloudtravel.shardingsphere" , sqlSessionTemplateRef = "testSqlSessionTemplate")
public class DataSourceConfig {

    private String shardDataSource0 ="cloudtravel_consumer1";
    private String shardDataSource1 ="cloudtravel_consumer2";

    private String defaultDataSource="cloudtravel_consumer1";

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    private String exeDatabaseShardingColumn = "TENANT_ID";

    private String executionShardingColumn = "BIZ_ID";

    @Autowired
    private TableShardingAlgorithm tableShardingAlgorithmCom;

    @Autowired
    private DatabaseShardingAlgorithm databaseShardingAlgorithm;
    /**
     * 设置数据源
     * @return
     * @throws SQLException
     */
    @Primary
    @Bean(name = "shardingDataSource")
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        // 设置默认的分库策略
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(
                new StandardShardingStrategyConfiguration(exeDatabaseShardingColumn, databaseShardingAlgorithm));
        //  配置表规则
        shardingRuleConfig.getTableRuleConfigs().
                addAll(Arrays.asList(
                        getBUserRuleConfiguration() ,
                        getTSpRuleConfiguration()
                        )
                );
        // 设置默认数据库
        shardingRuleConfig.setDefaultDataSourceName(defaultDataSource);

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    /**
     * 获取sqlSessionFactory实例
     * @param shardingDataSource
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(SqlSessionFactory sqlSessionFactory)  {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 需要手动配置事务管理器
     * @param shardingDataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(DataSource shardingDataSource) {
        return new DataSourceTransactionManager(shardingDataSource);
    }

    /**
     * 具体表进行分库分表的规则配置
     * @return
     */
    private TableRuleConfiguration getBUserRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration("b_user", "cloudtravel_consumer$->{1..2}.b_user");
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(exeDatabaseShardingColumn, databaseShardingAlgorithm));
        return orderTableRuleConfig;
    }

    /**
     * 具体表进行分库分表的规则配置
     * @return
     */
    private TableRuleConfiguration getTSpRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration("t_sp", "cloudtravel_consumer$->{1..2}.t_sp_$->{0..1}");
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(exeDatabaseShardingColumn, databaseShardingAlgorithm));
        orderTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration(executionShardingColumn,tableShardingAlgorithmCom));
        return orderTableRuleConfig;
    }


    @Bean("dataSource1")
    @ConfigurationProperties(prefix = "datasource.cloudtravel-consumer1")
    public DataSource druidDataSource1() {
        return new DruidDataSource();
    }

    @Bean("dataSource2")
    @ConfigurationProperties(prefix = "datasource.cloudtravel-consumer2")
    public DataSource druidDataSource2() {
        return new DruidDataSource();
    }

    /**
     * 多库的连接配置
     * @return
     */
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put(shardDataSource0,druidDataSource1());
        result.put(shardDataSource1, druidDataSource2());
        return result;
    }

}
