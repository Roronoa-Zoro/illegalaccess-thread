package com.illegalaccess.thread.sdk.alarm;


import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiao on 2019/12/28.
 * 通过任务的被拒绝次数进行报警
 */
public class ThreadPoolRejectAlarmStrategy implements ThreadPoolAlarmStrategy {
    private static final long serialVersionUID = -2715704871997116354L;

    private ConcurrentMap</*threadPoolName*/String, AtomicInteger> threadPoolRejectCounter = new ConcurrentHashMap<>();

    @Override
    public boolean triggerAlarm(ThreadTaskMetric metric, ThreadPoolAlarmConfig alarmConfig) {
        AtomicInteger counter = threadPoolRejectCounter.get(metric.getPoolName());
        if (counter == null) {
            threadPoolRejectCounter.putIfAbsent(metric.getPoolName(), new AtomicInteger(0));
            counter = threadPoolRejectCounter.get(metric.getPoolName());
        }
        int count = counter.incrementAndGet();
        if (count >= alarmConfig.getRejectNums()) {
            counter.getAndAdd(-alarmConfig.getRejectNums());
            return true;
        }

        return false;
    }
}
