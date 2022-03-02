package com.cloudtravel.shardingsphere.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 分库逻辑
 */
@Service
public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm {

    @Value("${SHARDING-DATABASE-TABLE-NAMES}")
    private String SHARDING_DATABASE_TABLE_NAMES;

    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        String dataSource = null;
        String tableName = preciseShardingValue.getLogicTableName();
        if(SHARDING_DATABASE_TABLE_NAMES.indexOf(tableName.toUpperCase()) == -1) {
            System.out.println(tableName + "不走分库,走默认库 cloudtravel_consumer1");
            return "cloudtravel_consumer1";
        }
        Integer value = StringUtils.isNotEmpty(preciseShardingValue.getValue().toString()) ?
                Integer.parseInt(preciseShardingValue.getValue().toString()) : 0;
        if (value % 2 == 0) {
            dataSource = "cloudtravel_consumer1";
        } else {
            dataSource = "cloudtravel_consumer2";
        }
        System.out.println(tableName + "走分库,tenantId = " + value + "进入" + dataSource);
        return dataSource;
    }
}
