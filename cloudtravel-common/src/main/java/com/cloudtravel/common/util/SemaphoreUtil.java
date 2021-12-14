package com.cloudtravel.common.util;

import com.cloudtravel.common.exception.CloudTravelException;
import com.cloudtravel.common.sourcecode.SemaphoreAnalyse;

/**
 * @author Administrator
 */
public abstract class SemaphoreUtil {

    public static final int count = 1;

    private static final SemaphoreAnalyse semaphore = new SemaphoreAnalyse(count);

    /**
     * 抽象方法执行任务
     * @return
     */
    public abstract Object execute();

    /**
     * 依赖信号量执行方法
     * @return
     */
    private Object executeWithSemaphore() {
        try {
            semaphore.acquireUninterruptibly();
            return execute();
        }catch (Exception e) {
            throw new CloudTravelException(e.getMessage());
        }finally {
            semaphore.release();
        }
    }
}
