package com.illegalaccess.thread.sdk.thread;

import com.illegalaccess.thread.sdk.support.TaskLifecycleTracer;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiao on 2019/12/20.
 */
public class TracedBoundedBlockingQueue<E> extends ArrayBlockingQueue<E> {

    private static final int DEFAULT_CAPACITY = 200;

    private String poolName;

    // 不和线程池搭配使用
    public TracedBoundedBlockingQueue() {
        super(DEFAULT_CAPACITY);
    }

    public TracedBoundedBlockingQueue(String poolName) {
        super(DEFAULT_CAPACITY);
        this.poolName = poolName;
    }

    public TracedBoundedBlockingQueue(String poolName, int capacity) {
        super(capacity);
        this.poolName = poolName;
    }

    public TracedBoundedBlockingQueue(String poolName, int capacity, boolean fair) {
        super(capacity, fair);
        this.poolName = poolName;
    }

    public TracedBoundedBlockingQueue(String poolName, int capacity, boolean fair, Collection<? extends E> c) {
        super(capacity, fair, c);
        this.poolName = poolName;
    }

    @Override
    public boolean add(E e) {
        boolean offered = super.add(e);
        if (offered) {
            TaskLifecycleTracer.taskEnQueue(poolName, e);
        }
        return offered;
    }

    @Override
    public void put(E e) throws InterruptedException {
        super.put(e);
        System.out.println(Thread.currentThread().getName() + " put a task");
        TaskLifecycleTracer.taskEnQueue(poolName, e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        boolean offered = super.offer(e, timeout, unit);
        if (offered) {
            System.out.println(Thread.currentThread().getName() + " offer a task with timeout");
            TaskLifecycleTracer.taskEnQueue(poolName, e);
        }
        return offered;
    }

    @Override
    public boolean offer(E e) {
//        System.out.println(Thread.currentThread().getName() + " will offer a task in " + poolName);
        boolean offered =  super.offer(e);
        if (offered) {
            System.out.println(Thread.currentThread().getName() + " offer a task=" + e);
            TaskLifecycleTracer.taskEnQueue(poolName, e);
        }
        return offered;
    }

    @Override
    public E take() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "=in " + poolName);
        E result = super.take();
        if (result != null) {
            TaskLifecycleTracer.taskOutOfQueue(poolName, result);
            System.out.println("take waiting in queue is:");
        }
        return result;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E result = super.poll(timeout, unit);
        if (result != null) {
            TaskLifecycleTracer.taskOutOfQueue(poolName, result);
            System.out.println("poll with timeout waiting in queue is:");
        }
        return result;
    }

    @Override
    public E poll() {
        E result = super.poll();
        if (result != null) {
            TaskLifecycleTracer.taskOutOfQueue(poolName, result);
            System.out.println("poll a task from in queue is:");
        }
        return result;
    }

    public String getPoolName() {
        return poolName;
    }
}
