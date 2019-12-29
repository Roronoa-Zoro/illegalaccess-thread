package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import com.illegalaccess.thread.sdk.support.MetricPipeline;

/**
 * Created by xiao on 2019/12/21.
 * 监听报警队列，判读是否符合报警条件，符合条件的调用 meta-server
 */
public class CollectAlarmTask implements Runnable {

    // todo, 监听报警队列，判读是否符合报警条件，符合条件的调用 meta-server  MetaServiceClient
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ThreadTaskMetric metric = MetricPipeline.Instance.fetch(MetricPipeline.Instance.INNER_THREAD_METRIC_QUEUE);
                if (metric == null) {
                    continue;
                }
                System.out.println("CollectAlarmTask=====" + metric);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
