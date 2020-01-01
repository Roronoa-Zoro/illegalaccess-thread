package com.illegalaccess.thread.sdk.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiao on 2019/12/20.
 */
public class TracedExecutors {

    public static ExecutorService newSingleThreadPoolExecutor(String threadPoolName) {
        // todo 添加拒绝策略
        return new TracedThreadPoolExecutor(threadPoolName, 1, 1, Integer.MAX_VALUE, TimeUnit.DAYS, new TracedBoundedBlockingQueue<>(threadPoolName));
    }

    /**
     * 从meta server根据threadPoolName获取配置
     * @param threadPoolName
     * @return
     */
    public static ExecutorService newThreadPoolExecutor(String threadPoolName) {
        // todo
        return null;
    }

    public static ExecutorService newThreadPoolExecutor(String threadPoolName, int coreSize, int maxSize, int keepAliveTime, TimeUnit unit, int queueLength) {
        return new TracedThreadPoolExecutor(threadPoolName, coreSize, maxSize, keepAliveTime, unit, new TracedBoundedBlockingQueue<>(threadPoolName, queueLength), new TracedThreadFactory(threadPoolName));
    }

    public static ExecutorService newThreadPoolExecutor(String threadPoolName, int coreSize, int maxSize, int keepAliveTime, TimeUnit unit, int queueLength, TracedRejectedExecutionHandler rejectedExecutionHandler) {
        return new TracedThreadPoolExecutor(threadPoolName, coreSize, maxSize, keepAliveTime, unit, new TracedBoundedBlockingQueue<>(threadPoolName, queueLength), new TracedThreadFactory(threadPoolName), rejectedExecutionHandler);
    }
}
