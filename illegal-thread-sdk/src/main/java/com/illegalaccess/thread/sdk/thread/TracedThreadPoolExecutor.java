package com.illegalaccess.thread.sdk.thread;


import com.illegalaccess.thread.sdk.support.NamedThreadPoolEventSource;
import com.illegalaccess.thread.sdk.support.TaskLifecycleTracer;

import java.util.concurrent.*;

/**
 * Created by xiao on 2019/12/20.
 */
public class TracedThreadPoolExecutor extends ThreadPoolExecutor {

    private String threadPoolName;

    public TracedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new TracedThreadFactory(threadPoolName), new TracedAbortPolicy());
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);
    }

    public TracedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new TracedAbortPolicy());
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);

    }

    public TracedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new TracedThreadFactory(threadPoolName), handler);
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);

    }

    public TracedThreadPoolExecutor(String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.threadPoolName = threadPoolName;
        NamedThreadPoolEventSource.Instance.publishNamedThreadPoolCreationEvent(this);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
//        System.out.println(Thread.currentThread().getName() + " beforeExecute............" + r);
        TaskLifecycleTracer.taskStartExec(threadPoolName, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        System.out.println(Thread.currentThread().getName() + " after............" + r);

        TaskLifecycleTracer.taskEndExec(threadPoolName, r);
    }

    @Override
    protected void terminated() {
        super.terminated();
    }

//    @Override
//    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
//        return super.newTaskFor(runnable, value);
//    }
//
//    @Override
//    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
//        return super.newTaskFor(callable);
//    }

//    @Override
//    public Future<?> submit(Runnable task) {
//        System.out.println(Thread.currentThread().getName() + " submit a task...");
//        if (task instanceof TraceRunnable) {
//            return super.submit(task);
//        }
//
//        TraceRunnable tr = new TraceRunnable(task);
//        return super.submit(tr);
//    }
//
//    @Override
//    public <T> Future<T> submit(Runnable task, T result) {
//        if (task instanceof TraceRunnable) {
//            return super.submit(task, result);
//        }
//        TraceRunnable tr = new TraceRunnable(task);
//        return super.submit(tr, result);
//    }
//
//    @Override
//    public <T> Future<T> submit(Callable<T> task) {
//        if (task instanceof TracedCallable) {
//            return super.submit(task);
//        }
//        TracedCallable tc = new TracedCallable(task);
//        return super.submit(tc);
//    }
//
//    @Override
//    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
//        return super.invokeAny(wrap(tasks));
//    }
//
//    @Override
//    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//        return super.invokeAny(wrap(tasks), timeout, unit);
//    }
//
//    @Override
//    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
//        return super.invokeAll(wrap(tasks));
//    }
//
//    @Override
//    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
//        return super.invokeAll(wrap(tasks), timeout, unit);
//    }
//
//    private <T> Collection<TracedCallable<T>> wrap(Collection<? extends Callable<T>> tasks) {
//        if (tasks == null || tasks.isEmpty()) {
//            return new ArrayList<>(0);
//        }
//
//        Collection<TracedCallable<T>> wrapedTask = new ArrayList<>(tasks.size());
//        tasks.forEach(ta -> {
//            TracedCallable<T> tc = new TracedCallable<>(ta);
//            wrapedTask.add(tc);
//        });
//        return wrapedTask;
//    }

    public String getThreadPoolName() {
        return threadPoolName;
    }


    public static class TracedAbortPolicy extends AbortPolicy implements TracedRejectedExecutionHandler {
        public TracedAbortPolicy() {
            super();
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            System.out.println("");
            super.rejectedExecution(r, e);
        }
    }

    public static class TracedDiscardPolicy extends DiscardPolicy implements TracedRejectedExecutionHandler {

    }

    public static class TracedDiscardOldestPolicy extends DiscardOldestPolicy implements TracedRejectedExecutionHandler {

    }
}
