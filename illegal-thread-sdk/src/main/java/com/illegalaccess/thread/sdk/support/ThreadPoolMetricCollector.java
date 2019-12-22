package com.illegalaccess.thread.sdk.support;

import com.illegalaccess.thread.sdk.bo.ThreadPoolReportBO;
import com.illegalaccess.thread.sdk.metric.ThreadPoolMetric;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by xiao on 2019/12/21.
 * 线程池和线程执行的任务的 数据指标 收集器
 */
public enum ThreadPoolMetricCollector {

    Instance;

    private CopyOnWriteArrayList<ThreadPoolMetric> threadPoolMetrics = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<ThreadTaskMetric> threadTaskMetrics = new CopyOnWriteArrayList<>();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void addThreadPoolMetric(ThreadPoolMetric threadPoolMetric) {
        if (readWriteLock.readLock().tryLock()) {
            try {
                threadPoolMetrics.add(threadPoolMetric);
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }

    public void addThreadTaskMetric(ThreadTaskMetric threadTaskMetric) {
        if (readWriteLock.readLock().tryLock()) {
            try {
                threadTaskMetrics.add(threadTaskMetric);
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }

    public ThreadPoolReportBO toReportBO() {
        readWriteLock.writeLock().lock();
        try {
            ThreadPoolReportBO reportBO = new ThreadPoolReportBO();

            reportBO.setThreadPoolMetrics(threadPoolMetrics);
            threadPoolMetrics.clear();
            reportBO.setThreadTaskMetrics(threadTaskMetrics);
            threadTaskMetrics.clear();
            return reportBO;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
