package com.illegalaccess.thread.sdk.thread;


import com.illegalaccess.thread.sdk.support.NamedThreadPoolEventSource;

import java.util.concurrent.*;

/**
 * Created by xiao on 2019/12/20.
 */
public class NamedThreadPoolExecutor extends ThreadPoolExecutor {

    private String threadPoolName;

    public NamedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);
    }

    public NamedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);

    }

    public NamedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);

    }

    public NamedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println("beforeExecute............" + r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    @Override
    protected void terminated() {
        super.terminated();
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }
}
