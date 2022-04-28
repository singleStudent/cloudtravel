package com.cloudtravel.db.config;

import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源路由事务一致性解决
 */
public class DynamicDataSourceTransaction implements Transaction{

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceTransaction.class);

    /** 当前线程的主数据源 */
    private DataSource dataSource;

    /** 线程当前的数据库连接 */
    private Connection mainConnection;

    /** 线程当前使用的数据源key */
    private DataSourceEnums mainDataSourceKey;

    /** 线程内的数据库连接 */
    private static ThreadLocal<ConcurrentHashMap<DataSourceEnums , Connection>> threadConnection = new ThreadLocal<>();

    private static ConcurrentHashMap<Object , Object> dataSourceMap = DataSourceConfig.dataSourceMap;

    /** 当前是否开启事务 */
    private boolean isConnectionTransactional;

    /** 当前线程是否自动提交 */
    private boolean autoCommit;

    private boolean closeFlag = false;

    public DynamicDataSourceTransaction(DataSource dataSource) {
        Assert.notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
        if(null == threadConnection.get()) {
            threadConnection.set(new ConcurrentHashMap<DataSourceEnums , Connection>());
        }
        mainDataSourceKey = DataSourceContext.getCurrentDataSourceType();
    }

    @Override
    public Connection getConnection() throws SQLException {
        DataSourceEnums dataSourceKeyNow = DataSourceContext.getCurrentDataSourceType();
        if(dataSourceKeyNow.equals(mainDataSourceKey)) {
            if (null != mainConnection) {
                return mainConnection;
            }else {
                this.mainConnection = DataSourceUtils.getConnection(this.dataSource);
                this.autoCommit = this.mainConnection.getAutoCommit();
                this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.mainConnection , this.dataSource);
                this.mainDataSourceKey = dataSourceKeyNow;
                return this.mainConnection;
            }
        }else {
            Connection connection = null;
            if(null != threadConnection.get() && !threadConnection.get().contains(dataSourceKeyNow)) {
                connection = DataSourceUtils.getConnection(dataSource);
                ConcurrentHashMap<DataSourceEnums , Connection> concurrentHashMap = threadConnection.get();
                concurrentHashMap.put(dataSourceKeyNow , connection);
                threadConnection.set(concurrentHashMap);
            }
            return connection;
        }
    }

    @Override
    public void commit() throws SQLException {
        if(null != this.mainConnection && !autoCommit && !this.mainConnection.isClosed()) {
            LOGGER.info("Commit JDBC CONNECTION : [{}]" , this.mainConnection);
            this.mainConnection.commit();
        }
        if(null != threadConnection.get() && !CollectionUtils.isEmpty(threadConnection.get())) {
            for (Connection connection : threadConnection.get().values()) {
                if(!connection.isClosed() && !connection.getAutoCommit()) {
                    LOGGER.info("Commit JDBC CONNECTION : [{}]" , connection);
                    connection.commit();
                }
            }
        }
        closeFlag = true;
    }

    @Override
    public void rollback() throws SQLException {
        if(null != this.mainConnection && !autoCommit && !this.mainConnection.isClosed()) {
            LOGGER.info("Rollback JDBC CONNECTION : [{}]" , this.mainConnection);
            this.mainConnection.rollback();
        }
        if(null != threadConnection && !CollectionUtils.isEmpty(threadConnection.get())) {
            for (Connection connection : threadConnection.get().values()) {
                if(!connection.isClosed() && !connection.getAutoCommit()) {
                    LOGGER.info("Rollback JDBC CONNECTION : [{}]" , connection);
                    connection.rollback();
                }
            }
        }
        closeFlag = true;
    }

    @Override
    public void close() throws SQLException {
        if(!closeFlag) {
            return;
        }
        if(null != this.mainConnection && !this.mainConnection.isClosed()) {
            LOGGER.info("Close JDBC CONNECTION : [{}]" , this.mainConnection);
            DataSourceUtils.releaseConnection(this.mainConnection , dataSource);
        }
        if(null != threadConnection && !CollectionUtils.isEmpty(threadConnection.get())) {
            for (Connection connection : threadConnection.get().values()) {
                if(!connection.isClosed()) {
                    connection.close();
                }
            }
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(this.dataSource);
        return holder != null && holder.hasTimeout() ? holder.getTimeToLiveInSeconds() : null;
    }
}
