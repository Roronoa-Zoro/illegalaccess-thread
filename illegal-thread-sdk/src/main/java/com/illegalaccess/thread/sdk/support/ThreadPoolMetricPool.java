package com.illegalaccess.thread.sdk.support;

import com.illegalaccess.thread.sdk.bo.ThreadPoolReportReq;
import com.illegalaccess.thread.sdk.metric.ThreadPoolMetric;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by xiao on 2019/12/21.
 * 线程池和线程执行的任务的 数据指标 收集器
 */
public enum ThreadPoolMetricPool {

    Instance;

    private CopyOnWriteArrayList<ThreadPoolMetric> threadPoolMetrics = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<ThreadTaskMetric> threadTaskMetrics = new CopyOnWriteArrayList<>();

//    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void addThreadPoolMetric(ThreadPoolMetric threadPoolMetric) {
        threadPoolMetrics.add(threadPoolMetric);

//        if (readWriteLock.readLock().tryLock()) {
//            try {
//
//            } finally {
//                readWriteLock.readLock().unlock();
//            }
//        }
    }

    public void addThreadTaskMetric(ThreadTaskMetric threadTaskMetric) {
        threadTaskMetrics.add(threadTaskMetric);
//        if (readWriteLock.readLock().tryLock()) {
//            try {
//
//            } finally {
//                readWriteLock.readLock().unlock();
//            }
//        }
    }

    public ThreadPoolReportReq toReportBO() {
        ThreadPoolReportReq reportBO = new ThreadPoolReportReq();

        reportBO.setThreadPoolMetrics(threadPoolMetrics);
        threadPoolMetrics.clear();
        reportBO.setThreadTaskMetrics(threadTaskMetrics);
        threadTaskMetrics.clear();
        return reportBO;

//        readWriteLock.writeLock().lock();
//        try {
//
//        } finally {
//            readWriteLock.writeLock().unlock();
//        }
    }
}
