package com.cloudtravel.shardingsphere.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * 分库逻辑
 */
@Service
public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm {

    @Autowired
    private DataSourceConfigBase dataSourceConfigBase;

    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        String dataSource = null;
        String tableName = preciseShardingValue.getLogicTableName();
        if(!dataSourceConfigBase.getShardingDatabaseTableNames().contains(tableName.toUpperCase())) {
            System.out.println(tableName + "不走分库,走默认库" + dataSourceConfigBase.getDatasourceName1());
            return dataSourceConfigBase.getDatasourceName1();
        }
        Integer value = StringUtils.isNotEmpty(preciseShardingValue.getValue().toString()) ?
                Integer.parseInt(preciseShardingValue.getValue().toString()) : 0;
        if (value % 2 == 0) {
            dataSource = dataSourceConfigBase.getDatasourceName1();
        } else {
            dataSource = dataSourceConfigBase.getDatasourceName2();
        }
        System.out.println(tableName + "走分库,tenantId = " + value + "进入" + dataSource);
        return dataSource;
    }
}
