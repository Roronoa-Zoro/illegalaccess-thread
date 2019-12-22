package com.illegalaccess.thread.sdk.thread;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiao on 2019/12/20.
 */
public class NamedBoundedBlockingQueue<E> extends ArrayBlockingQueue<E> {

    private static final int DEFAULT_CAPACITY = 200;

    private String poolName;

    // 不和线程池搭配使用
    public NamedBoundedBlockingQueue() {
        super(DEFAULT_CAPACITY);
    }

    public NamedBoundedBlockingQueue(String poolName) {
        super(DEFAULT_CAPACITY);
        this.poolName = poolName;
    }

    public NamedBoundedBlockingQueue(String poolName, int capacity) {
        super(capacity);
        this.poolName = poolName;
    }

    public NamedBoundedBlockingQueue(String poolName, int capacity, boolean fair) {
        super(capacity, fair);
        this.poolName = poolName;
    }

    public NamedBoundedBlockingQueue(String poolName, int capacity, boolean fair, Collection<? extends E> c) {
        super(capacity, fair, c);
        this.poolName = poolName;
    }

    @Override
    public boolean add(E e) {
        System.out.println("add........");
        boolean offered = super.add(e);
        if (offered) {
            TaskWaiting.putStartTime(e, System.currentTimeMillis());
        }
        return offered;
    }

    @Override
    public void put(E e) throws InterruptedException {
        System.out.println("put........");
        super.put(e);
        TaskWaiting.putStartTime(e, System.currentTimeMillis());
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        System.out.println("offer timeout........");
        boolean offered = super.offer(e, timeout, unit);
        if (offered) {
            TaskWaiting.putStartTime(e, System.currentTimeMillis());
        }
        return offered;
    }

    @Override
    public boolean offer(E e) {
        System.out.println("offer........" + e);
        boolean offered =  super.offer(e);
        if (offered) {
            TaskWaiting.putStartTime(e, System.currentTimeMillis());
        }
        return offered;
    }

    @Override
    public E take() throws InterruptedException {

        E result = super.take();
        System.out.println("take...." + result);
        if (result != null) {
            Long start = TaskWaiting.clearTask(result);
            System.out.println("task waiting in queue is:" + (System.currentTimeMillis() - start));
        }
        return result;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        System.out.println("poll timeout....");
        E result = super.poll(timeout, unit);
        if (result != null) {
            Long start = TaskWaiting.clearTask(result);
            System.out.println("task waiting in queue is:" + (System.currentTimeMillis() - start));
        }
        return result;
    }

    @Override
    public E poll() {
        System.out.println("poll....");
        E result = super.poll();
        if (result != null) {
            Long start = TaskWaiting.clearTask(result);
            System.out.println("task waiting in queue is:" + (System.currentTimeMillis() - start));
        }
        return result;
    }
}
