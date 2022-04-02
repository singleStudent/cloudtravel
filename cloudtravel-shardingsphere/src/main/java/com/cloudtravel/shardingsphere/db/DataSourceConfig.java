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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.cloudtravel.shardingsphere.dao" , sqlSessionTemplateRef = "testSqlSessionTemplate")
@Order(11)
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    private TableShardingAlgorithm tableShardingAlgorithmCom;

    @Autowired
    private DatabaseShardingAlgorithm databaseShardingAlgorithm;

    @Autowired
    DataSourceConfigBase dataSourceConfigBase;

    @Autowired
    Environment environment;

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
                new StandardShardingStrategyConfiguration(
                        dataSourceConfigBase.getDatabaseShardingColumnDefault(),
                        databaseShardingAlgorithm
                )
        );
        //  配置表规则
        shardingRuleConfig.getTableRuleConfigs().
                addAll(Arrays.asList(
                        getBUserRuleConfiguration() ,
                        getTSpRuleConfiguration()
                        )
                );
        // 设置默认数据库
        shardingRuleConfig.setDefaultDataSourceName(dataSourceConfigBase.getDatasourceName1());

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    /**
     * 获取sqlSessionFactory实例
     * @param shardingDataSource
     * @return
     * @throws Exception
     */
    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("shardingDataSource") DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(dataSourceConfigBase.getMapperLocations()));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sqlSessionFactory")SqlSessionFactory sqlSessionFactory)  {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 需要手动配置事务管理器
     * @param shardingDataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(@Qualifier("shardingDataSource") DataSource shardingDataSource) {
        return new DataSourceTransactionManager(shardingDataSource);
    }

    /**
     * 具体表进行分库分表的规则配置
     * @return
     */
    private TableRuleConfiguration getBUserRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration("b_user",
                dataSourceConfigBase.getUserActualDataNodes());
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(
                new StandardShardingStrategyConfiguration(
                        dataSourceConfigBase.getDatabaseShardingColumnDefault(),
                        databaseShardingAlgorithm
                )
        );
        return orderTableRuleConfig;
    }

    /**
     * 具体表进行分库分表的规则配置
     * @return
     */
    private TableRuleConfiguration getTSpRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration("t_sp",
                dataSourceConfigBase.getSpActualDataNodes());
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(
                new StandardShardingStrategyConfiguration(
                        dataSourceConfigBase.getDatabaseShardingColumnDefault(),
                        databaseShardingAlgorithm
                )
        );
        orderTableRuleConfig.setTableShardingStrategyConfig(
                new ComplexShardingStrategyConfiguration(
                        dataSourceConfigBase.getTableSpShardingColumn(),
                        tableShardingAlgorithmCom
                )
        );
        return orderTableRuleConfig;
    }


    public DataSource druidDataSource1() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(environment.getProperty("datasource.cloudtravel-consumer1.url"));
        dataSource.setUsername(environment.getProperty("datasource.cloudtravel-consumer1.username"));
        dataSource.setPassword(environment.getProperty("datasource.cloudtravel-consumer1.password"));
        dataSource.setDriverClassName(environment.getProperty("datasource.cloudtravel-consumer1.driver-class-name"));
        return dataSource;
    }

    public DataSource druidDataSource2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(environment.getProperty("datasource.cloudtravel-consumer2.url"));
        dataSource.setUsername(environment.getProperty("datasource.cloudtravel-consumer2.username"));
        dataSource.setPassword(environment.getProperty("datasource.cloudtravel-consumer2.password"));
        dataSource.setDriverClassName(environment.getProperty("datasource.cloudtravel-consumer2.driver-class-name"));
        return dataSource;
    }

    /**
     * 多库的连接配置
     * @return
     */
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put(dataSourceConfigBase.getDatasourceName1(),druidDataSource1());
        result.put(dataSourceConfigBase.getDatasourceName2(), druidDataSource2());
        return result;
    }
}
