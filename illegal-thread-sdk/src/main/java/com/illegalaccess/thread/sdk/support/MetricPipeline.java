package com.illegalaccess.thread.sdk.support;

import com.illegalaccess.thread.sdk.bo.ThreadPoolReportReq;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import com.illegalaccess.thread.sdk.thread.TracedBoundedBlockingQueue;


import java.util.concurrent.*;

/**
 * Created by xiao on 2019/12/21.
 * 指标数据pipeline
 */
public enum MetricPipeline {

    Instance;

    private ConcurrentMap<String, TracedBoundedBlockingQueue> queueHolder;
    public final String INNER_SAVE_POINT_QUEUE = "innerSavePointQueue";
    public final String INNER_THREAD_METRIC_QUEUE = "innerThreadMetricQueue";
    public final String INNER_REPORT_TASK_QUEUE = "innerThreadReportQueue";

    MetricPipeline() {
        queueHolder = new ConcurrentHashMap<>();
        // 保存执行过程中，各个生命周期的时间点  的集合
        queueHolder.put(INNER_SAVE_POINT_QUEUE, new TracedBoundedBlockingQueue<TaskLifecycleSavepoint>(INNER_SAVE_POINT_QUEUE));
        // 数据类型和 <taskMetric> 一样，这个的数据接受者进行是否需要报警的计算
        queueHolder.put(INNER_THREAD_METRIC_QUEUE, new TracedBoundedBlockingQueue<ThreadTaskMetric>(INNER_THREAD_METRIC_QUEUE));
        // 收集好的 需要上报的数据
        queueHolder.put(INNER_REPORT_TASK_QUEUE, new TracedBoundedBlockingQueue<ThreadPoolReportReq>(INNER_REPORT_TASK_QUEUE));
    }

    /**
     * 发送消息
     * @param source
     * @param data
     * @param <T>
     */
    public <T> void emit(String source, T data) {
        TracedBoundedBlockingQueue queue = queueHolder.get(source);
        if (queue == null) {
            throw new IllegalArgumentException("source[" + source + "] is invalid");
        }
        queue.offer(data);
    }

    /**
     * 获取消息
     * @param source
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> T fetch(String source) throws InterruptedException {
        TracedBoundedBlockingQueue queue = queueHolder.get(source);
        if (queue == null) {
            throw new IllegalArgumentException("source[" + source + "] is invalid");
        }
        Object data = queue.poll(100, TimeUnit.MILLISECONDS);
        if (data == null) {
            return null;
        }
        return (T) data;
    }

}
