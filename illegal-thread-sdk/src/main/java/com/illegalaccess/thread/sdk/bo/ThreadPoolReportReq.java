package com.illegalaccess.thread.sdk.bo;

import com.illegalaccess.thread.sdk.metric.ThreadPoolMetric;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import lombok.Getter;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xiao on 2019/12/21.
 */
@Getter
public class ThreadPoolReportReq implements Serializable {

    private static final long serialVersionUID = -6833846058207747662L;

    private CopyOnWriteArrayList<ThreadPoolMetric> threadPoolMetrics = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<ThreadTaskMetric> threadTaskMetrics = new CopyOnWriteArrayList<>();

    public void setThreadPoolMetrics(CopyOnWriteArrayList<ThreadPoolMetric> threadPoolMetrics) {
        this.threadPoolMetrics.addAll(threadPoolMetrics);
    }

    public void setThreadTaskMetrics(CopyOnWriteArrayList<ThreadTaskMetric> threadTaskMetrics) {
        this.threadTaskMetrics = threadTaskMetrics;
    }
}
