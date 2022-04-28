package com.cloudtravel.db.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 数据源上下文对象
 */
public class DataSourceContext {

    private static final ThreadLocal<DataSourceEnums> currentDataSource = new ThreadLocal<>();

    /** 根据tenantId进行数据源路由策略: (tenantId % 2) +1 */
    private static final int DB_ROUT_FIX = 2;


    /**
     * 设置当前线程中的数据源
     * @param dataSourceEnum
     */
    public static void setDataSourceType(DataSourceEnums dataSourceEnum) {
        System.out.println("change dataSource type to " + dataSourceEnum);
        currentDataSource.set(dataSourceEnum);
    }

    /**
     * 设置当前线程中的数据源
     * @param tenantId
     */
    public static void setDataSourceType(String tenantId) {
        Assert.state(StringUtils.isNotBlank(tenantId) && StringUtils.isNumeric(tenantId) ,
                "Unsupported tenantId : " + tenantId);
        int tenantIdInt = Integer.valueOf(tenantId);
        DataSourceEnums dataSourceEnum = null;
        switch (tenantIdInt % DB_ROUT_FIX){
            case 0: dataSourceEnum = DataSourceEnums.CLOUDTRAVEL_CONSUMER1;
            break;
            case 1: dataSourceEnum = DataSourceEnums.CLOUDTRAVEL_CONSUMER2;
            break;
        }
        setDataSourceType(dataSourceEnum);
    }

    /**
     * 获取当前线程中设置的db类型
     * @return
     */
    public static DataSourceEnums getCurrentDataSourceType() {
        DataSourceEnums dataSourceEnum = currentDataSource.get();
        System.out.println("Current threadlocal db type is " + dataSourceEnum);
        dataSourceEnum = null != dataSourceEnum ? dataSourceEnum : DataSourceEnums.CLOUDTRAVEL_CONSUMER1;
        return dataSourceEnum;
    }

    public static void clearDbType() {
        currentDataSource.remove();
    }
}
