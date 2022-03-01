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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.cloudtravel.shardingsphere" , sqlSessionTemplateRef = "testSqlSessionTemplate")
public class DataSourceConfig {


    @Value("${spring.shardingsphere.sharding.tables.b_user.actual-data-nodes")
    private String executionActualDataNodes;

    private String executionTable ="b_user";

    private String shardDataSource0 ="cloudtravel_consumer1";
    private String shardDataSource1 ="cloudtravel_consumer2";

    private String defaultDataSource="cloudtravel_consumer2";

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${spring.shardingsphere.datasource.cloudtravel_consumer1.username}")
    private String username1;
    @Value("${spring.shardingsphere.datasource.cloudtravel_consumer1.url}")
    private String url1;
    @Value("${spring.shardingsphere.datasource.cloudtravel_consumer1.password}")
    private String password1;

    @Value("${spring.shardingsphere.datasource.cloudtravel_consumer2.username}")
    private String username2;
    @Value("${spring.shardingsphere.datasource.cloudtravel_consumer2.url}")
    private String url2;
    @Value("${spring.shardingsphere.datasource.cloudtravel_consumer2.password}")
    private String password2;

    @Value("${spring.shardingsphere.sharding.tables.wf_core_run_execution.database-strategy.inline.sharding-column:tenant_id_}")
    private String exeDatabaseShardingColumn;
    @Value("${spring.shardingsphere.sharding.tables.wf_core_run_execution.table_strategy.complex.shardingColumns:id_}")
    private String executionShardingColumn;

    @Autowired
    private ComplexTableExecutionShardingAlgorithm tableShardingAlgorithmCom;
    @Autowired
    private DatabaseShardingAlgorithm preciseModuloDatabaseShardingAlgorithm;
    /**
     * 设置数据源
     * @return
     * @throws SQLException
     */
    @Primary
    @Bean(name = "shardingDataSource")
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getBindingTableGroups().add(executionTable);
        //  配置表规则
        shardingRuleConfig.getTableRuleConfigs().add(getExeTableRuleConfiguration());
        // 设置默认的分库分表策略
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(exeDatabaseShardingColumn, preciseModuloDatabaseShardingAlgorithm));
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
    private TableRuleConfiguration getExeTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration(executionTable, executionActualDataNodes);
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(exeDatabaseShardingColumn, preciseModuloDatabaseShardingAlgorithm));
        orderTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration(executionShardingColumn,tableShardingAlgorithmCom));
        return orderTableRuleConfig;
    }


    @Primary
    @Bean("dataSource1")
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.cloudtravel_consumer1")
    public DataSource druidDataSource1() {
        return new DruidDataSource();
    }

    @Bean("dataSource2")
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.cloudtravel_consumer2")
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
