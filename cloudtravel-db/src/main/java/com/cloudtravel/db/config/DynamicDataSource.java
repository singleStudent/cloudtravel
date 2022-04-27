package com.cloudtravel.db.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.util.Map;

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

    /**
     * 将数据源对象集设置入AbstractRoutingDataSource中,便于后续根据当前数据源key切换数据源
     * @param targetDataSources
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(DataSourceConfig.dataSourceMap);
    }

    /**
     * 设置默认数据源
     * @param defaultTargetDataSource
     */
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(DataSourceConfig.dataSourceMap.get(DataSourceEnums.CLOUDTRAVEL_CONSUMER1));
    }
}
