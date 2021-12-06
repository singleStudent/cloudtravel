package com.cloudtravel.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 抽象callable
 * @author Administrator
 */
public abstract class AbstractThreadCall implements Callable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractThreadCall.class);

    /**
     * 抽离业务参数代码.统一结构为json
     *
     * @return
     */
    public Object execute() {
        return "success";
    }

    public void logPrint(String logBase, Boolean error, Object... args) {
        if (error) {
            LOGGER.error(logBase, args);
        } else {
            LOGGER.info(logBase, args);
        }
    }

    @Override
    public Object call() {
        try {
            return this.execute();
        } catch (Throwable e) {
            throw new RuntimeException("执行异常.......");
        }
    }
}
