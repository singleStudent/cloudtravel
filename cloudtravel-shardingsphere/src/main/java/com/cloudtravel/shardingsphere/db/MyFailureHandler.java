package com.cloudtravel.shardingsphere.db;

import io.seata.core.exception.TransactionException;
import io.seata.tm.api.FailureHandler;
import io.seata.tm.api.GlobalTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyFailureHandler implements FailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyFailureHandler.class);

    @Override
    public void onBeginFailure(GlobalTransaction globalTransaction, Throwable throwable){
        try {
            LOGGER.error("全局分布式事务开启失败 onBeginFailure , XId = {},localStatus ={},  message = {}" ,
                    globalTransaction.getXid() , globalTransaction.getStatus(), throwable.getMessage());
        }catch (TransactionException e) {
            LOGGER.error("全局分布式事务开启失败 ,globalTransaction.getStatus() error , message = {}" , e.getMessage());
        }

    }

    @Override
    public void onCommitFailure(GlobalTransaction globalTransaction, Throwable throwable) {
        try {
            LOGGER.error("全局分布式事务提交失败 onCommitFailure,  XId = {},localStatus ={},  message = {}" ,
                    globalTransaction.getXid() , globalTransaction.getStatus(), throwable.getMessage());
        }catch (TransactionException e) {
            LOGGER.error("全局分布式事务提交失败 ,globalTransaction.getStatus() error , message = {}" , e.getMessage());
        }
    }

    @Override
    public void onRollbackFailure(GlobalTransaction globalTransaction, Throwable throwable) {
        try {
            LOGGER.error("全局分布式事务回滚失败 onRollbackFailure,  XId = {},localStatus ={},  message = {}" ,
                    globalTransaction.getXid() , globalTransaction.getStatus(), throwable.getMessage());
        }catch (TransactionException e) {
            LOGGER.error("全局分布式事务回滚失败 ,globalTransaction.getStatus() error , message = {}" , e.getMessage());
        }
    }

    @Override
    public void onRollbackRetrying(GlobalTransaction globalTransaction, Throwable throwable) {
        try {
            LOGGER.error("全局分布式事务回滚尝试 onRollbackRetrying,  XId = {},localStatus ={},  message = {}" ,
                    globalTransaction.getXid() , globalTransaction.getStatus(), throwable.getMessage());
            globalTransaction.rollback();
        }catch (TransactionException e) {
            LOGGER.error("全局分布式事务回滚尝试 ,globalTransaction.getStatus() error , message = {}" , e.getMessage());
        }
    }
}
