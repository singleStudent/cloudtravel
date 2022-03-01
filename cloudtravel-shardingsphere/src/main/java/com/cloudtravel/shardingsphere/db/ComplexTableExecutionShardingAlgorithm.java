package com.cloudtravel.shardingsphere.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ComplexTableExecutionShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    @Override
    public Collection<String> doSharding(Collection collection, ComplexKeysShardingValue complexKeysShardingValue) {
        List<String> tables = new ArrayList<>();
        int id = StringUtils.isEmpty(((List) complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("FOUNDER_ACCOUNT_ID")).get(0).toString())
                ? 0 : 1;
        String physicsTable = complexKeysShardingValue.getLogicTableName() + "_";
        physicsTable += String.valueOf(id % 2);
        tables.add(physicsTable);
        return tables;
    }
}
