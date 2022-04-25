package com.cloudtravel.db.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 获取当前数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContext.getCurrentDataSourceType();
    }
}
