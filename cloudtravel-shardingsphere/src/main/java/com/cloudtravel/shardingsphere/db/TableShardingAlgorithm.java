package com.cloudtravel.shardingsphere.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *分表逻辑
 */
@Service
public class TableShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    @Value("${SHARDING-TABLE-NAMES}")
    private String SHARDING_TABLE_NAMES;

    @Override
    public Collection<String> doSharding(Collection collection, ComplexKeysShardingValue complexKeysShardingValue) {
        List<String> tables = new ArrayList<>();
        Long bizId = (Long) ((List) complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("biz_id")).get(0);
        String tableName = complexKeysShardingValue.getLogicTableName();
        String physicsTable = tableName  + "_";
        physicsTable += String.valueOf(bizId % 2);
        tables.add(physicsTable);
        System.out.println(tableName + "走分表,bizId = " + bizId + "路由到表" + physicsTable);
        return tables;
    }
}
