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
import java.util.ArrayList;
import java.util.List;
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
    private static ThreadLocal<List<Connection>> threadConnection = new ThreadLocal<>();

    private static ConcurrentHashMap<Object , Object> dataSourceMap = DataSourceConfig.dataSourceMap;

    /** 当前是否开启事务 */
    private boolean isConnectionTransactional;

    /** 当前线程是否自动提交 */
    private boolean autoCommit;

    /** 是否已经都提交或者回滚完毕 */
    private boolean commitOrRollbackDone;

    public DynamicDataSourceTransaction(DataSource dataSource) {
        Assert.notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
        List<Connection> connections = threadConnection.get();
        if(CollectionUtils.isEmpty(connections)) {
            threadConnection.set(new ArrayList<>());
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
                LOGGER.info("Current dataSource connection message , [{}] will {} be managed by spring" , this.mainConnection , this.isConnectionTransactional);
                this.mainDataSourceKey = dataSourceKeyNow;
                return this.mainConnection;
            }
        }else {
            DataSource dataSource = (DataSource)dataSourceMap.get(dataSourceKeyNow);
            Connection connection = DataSourceUtils.getConnection(dataSource);
            List<Connection> threadConnectionList = null;
            if(!(threadConnectionList = threadConnection.get()).contains(connection)) {
                threadConnectionList.add(connection);
                threadConnection.set(threadConnectionList);
            }
            return connection;
        }
    }

    @Override
    public void commit() throws SQLException {
        if(null != this.mainConnection && !autoCommit) {
            LOGGER.info("Commit JDBC CONNECTION : [{}]" , this.mainConnection);
            this.mainConnection.commit();
        }
        if(null != threadConnection && !CollectionUtils.isEmpty(threadConnection.get())) {
            for (Connection connection : threadConnection.get()) {
                if(!connection.isClosed() && !connection.getAutoCommit()) {
                    LOGGER.info("Commit JDBC CONNECTION : [{}]" , connection);
                    connection.commit();
                }
            }
        }
        commitOrRollbackDone = true;
    }

    @Override
    public void rollback() throws SQLException {
        if(null != this.mainConnection && !autoCommit) {
            LOGGER.info("Rollback JDBC CONNECTION : [{}]" , this.mainConnection);
            this.mainConnection.rollback();
        }
        if(null != threadConnection && !CollectionUtils.isEmpty(threadConnection.get())) {
            for (Connection connection : threadConnection.get()) {
                if(!connection.isClosed() && !connection.getAutoCommit()) {
                    LOGGER.info("Rollback JDBC CONNECTION : [{}]" , connection);
                    connection.rollback();
                }
            }
        }
        commitOrRollbackDone = true;
    }

    @Override
    public void close() throws SQLException {
        Assert.isTrue(commitOrRollbackDone , "Current thread has other connection is working!");
        if(null != this.mainConnection && !this.mainConnection.isClosed()) {
            LOGGER.info("Close JDBC CONNECTION : [{}]" , this.mainConnection);
            DataSourceUtils.releaseConnection(this.mainConnection , dataSource);
        }
        if(null != threadConnection && !CollectionUtils.isEmpty(threadConnection.get())) {
            for (Connection connection : threadConnection.get()) {
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
