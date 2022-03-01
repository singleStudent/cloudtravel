package com.cloudtravel.shardingsphere.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm {

    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        String dataSource = null;
        Integer value = StringUtils.isNotEmpty(preciseShardingValue.getValue().toString()) ?
                Integer.parseInt(preciseShardingValue.getValue().toString()) : 0;
        if (value == 0) {
            dataSource = "cloudtravel_consumer1";
        } else {
            dataSource = "cloudtravel_consumer2";
        }
        return dataSource;
    }
}
