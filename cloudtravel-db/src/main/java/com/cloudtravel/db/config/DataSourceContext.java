package com.cloudtravel.db.config;

/**
 * 数据源上下文对象
 */
public class DataSourceContext {

    private static final ThreadLocal<DataSourceEnums> currentDataSource = new ThreadLocal<>();

    /**
     * 设置当前线程中的数据源
     * @param dataSourceEnum
     */
    public static void setDataSourceType(DataSourceEnums dataSourceEnum) {
        System.out.println("change dataSource type to " + dataSourceEnum);
        currentDataSource.set(dataSourceEnum);
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
