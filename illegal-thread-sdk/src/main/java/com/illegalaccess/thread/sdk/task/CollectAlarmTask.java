package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.alarm.StrategyContext;
import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmReq;
import com.illegalaccess.thread.sdk.client.MetaClientFactory;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import com.illegalaccess.thread.sdk.support.MetricPipeline;
import com.illegalaccess.thread.sdk.thread.TracedThreadPoolManager;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by xiao on 2019/12/21.
 * 监听报警队列，判读是否符合报警条件，符合条件的调用 meta-server
 */
@Slf4j
public class CollectAlarmTask implements Runnable {

    private TracedThreadPoolManager tracedThreadPoolManager;

    public CollectAlarmTask(TracedThreadPoolManager tracedThreadPoolManager) {
        this.tracedThreadPoolManager = tracedThreadPoolManager;
    }

    // todo, 监听报警队列，判读是否符合报警条件，符合条件的调用 meta-server  MetaServiceClient
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ThreadTaskMetric metric = MetricPipeline.Instance.fetch(MetricPipeline.Instance.INNER_THREAD_METRIC_QUEUE);
                if (metric == null) {
                    continue;
                }
                ThreadPoolAlarmConfig alarmConfig = tracedThreadPoolManager.pickUpAlarmConfig(metric.getPoolName());
                if (alarmConfig == null) {
                    log.error("there is not alarm config for threadPoolName:{}", metric.getPoolName());
                }
                boolean triggered = StrategyContext.Instance.dispatch(metric, alarmConfig);
                if (triggered) {
                    // 同步调用 是否有必要改成异步
                    MetaClientFactory.getClient().reportThreadPoolAlarm(ThreadPoolAlarmReq.builder().poolName(metric.getPoolName()).build());
                }
                log.debug("CollectAlarmTask====={}",  metric);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("CollectAlarmTask.run fails", e);
            }
        }
    }
}
