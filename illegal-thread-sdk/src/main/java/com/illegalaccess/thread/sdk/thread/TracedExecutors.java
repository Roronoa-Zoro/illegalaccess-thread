package com.illegalaccess.thread.sdk.thread;

import com.illegalaccess.thread.sdk.bo.ThreadPoolConfig;
import com.illegalaccess.thread.sdk.client.MetaClientFactory;
import com.illegalaccess.thread.sdk.support.NamedThreadPoolEventSource;

import java.util.Arrays;
import java.util.List;
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
        ThreadPoolConfig config = MetaClientFactory.getClient().fetchThreadPoolConfig(threadPoolName);
        if (config == null) {
            throw new IllegalArgumentException("threadPoolName:{} is not configured");
        }

        TracedThreadPoolExecutor executor = new TracedThreadPoolExecutor(threadPoolName, config.getCoreSize(), config.getMaxSize(), config.getKeepAliveTime(), TimeUnit.SECONDS, new TracedBoundedBlockingQueue<>(threadPoolName, config.getQueueLength()), new TracedThreadFactory(threadPoolName));
        NamedThreadPoolEventSource.Instance.publishThreadPoolConfigChangeEvent(Arrays.asList(config));
        return executor;

    }

    public static ExecutorService newThreadPoolExecutor(String threadPoolName, int coreSize, int maxSize, int keepAliveTime, TimeUnit unit, int queueLength) {
        return new TracedThreadPoolExecutor(threadPoolName, coreSize, maxSize, keepAliveTime, unit, new TracedBoundedBlockingQueue<>(threadPoolName, queueLength), new TracedThreadFactory(threadPoolName));
    }

    public static ExecutorService newThreadPoolExecutor(String threadPoolName, int coreSize, int maxSize, int keepAliveTime, TimeUnit unit, int queueLength, TracedRejectedExecutionHandler rejectedExecutionHandler) {
        return new TracedThreadPoolExecutor(threadPoolName, coreSize, maxSize, keepAliveTime, unit, new TracedBoundedBlockingQueue<>(threadPoolName, queueLength), new TracedThreadFactory(threadPoolName), rejectedExecutionHandler);
    }
}
