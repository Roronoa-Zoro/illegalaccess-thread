package com.illegalaccess.thread.sdk.support;

import com.illegalaccess.thread.sdk.bo.ThreadPoolReportReq;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import com.illegalaccess.thread.sdk.thread.NamedBoundedBlockingDeque;
import com.illegalaccess.thread.sdk.thread.NamedBoundedBlockingQueue;
import com.illegalaccess.thread.sdk.utils.SdkConstants;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiao on 2019/12/21.
 * 指标数据pipeline
 */
public enum MetricPipeline {

    Instance;

    private BlockingDeque<ThreadTaskMetric> blockingDeque = new NamedBoundedBlockingDeque<>();

    // todo 有瑕疵， 并没有和线程池挂钩
    private BlockingQueue<ThreadPoolReportReq> metricReportQueue = new NamedBoundedBlockingQueue<>(SdkConstants.INNER_POOL);

    public void transferThreadTaskMetric(ThreadTaskMetric threadTaskMetric) {
        blockingDeque.add(threadTaskMetric);
    }

    public void revertThreadTaskMetric(ThreadTaskMetric threadTaskMetric) {
        blockingDeque.addFirst(threadTaskMetric);
    }

    public ThreadTaskMetric fetchThreadTaskMetric() throws InterruptedException {
        return blockingDeque.poll(100, TimeUnit.MILLISECONDS);
    }

    public void transferThreadMetricReport(ThreadPoolReportReq reportBO) {
        metricReportQueue.offer(reportBO);
    }

    public ThreadPoolReportReq fetchThreadMetricReport() throws InterruptedException {
        return metricReportQueue.poll(100, TimeUnit.MILLISECONDS);
    }

}
