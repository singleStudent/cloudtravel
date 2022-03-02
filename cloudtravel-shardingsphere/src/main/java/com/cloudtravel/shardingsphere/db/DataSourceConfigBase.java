package com.cloudtravel.shardingsphere.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(10)
public class DataSourceConfigBase {

    @Value("${sharding.datasource1.name}")
    private String datasourceName1;

    @Value("${SHARDING.datasource2.name}")
    private String datasourceName2;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${database.sharding.column}")
    private String databaseShardingColumnDefault;

    @Value("${SHARDING-DATABASE-TABLE-NAMES}")
    private String shardingDatabaseTableNames;

    @Value("${SHARDING-TABLE-NAMES}")
    private String shardingTableNames;

    @Value("${table.user.actual-data-nodes}")
    private String userActualDataNodes;

    @Value("${table.sp.actual-data-nodes}")
    private String spActualDataNodes;

    @Value("${table.sp.sharding.column}")
    private String tableSpShardingColumn;

    public String getDatasourceName1() {
        return datasourceName1;
    }

    public String getDatasourceName2() {
        return datasourceName2;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public String getDatabaseShardingColumnDefault() {
        return databaseShardingColumnDefault;
    }

    public String getShardingDatabaseTableNames() {
        return shardingDatabaseTableNames;
    }

    public String getShardingTableNames() {
        return shardingTableNames;
    }

    public String getUserActualDataNodes() {
        return userActualDataNodes;
    }

    public String getSpActualDataNodes() {
        return spActualDataNodes;
    }

    public String getTableSpShardingColumn() {
        return tableSpShardingColumn;
    }
}
