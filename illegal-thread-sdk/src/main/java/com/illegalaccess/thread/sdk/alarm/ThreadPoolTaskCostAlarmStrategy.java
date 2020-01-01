package com.illegalaccess.thread.sdk.alarm;

import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiao on 2019/12/28.
 */
public class ThreadPoolTaskCostAlarmStrategy implements ThreadPoolAlarmStrategy {

    private ConcurrentMap</*threadPoolName*/String, AtomicInteger> threadPoolTaskCounter = new ConcurrentHashMap<>();

    @Override
    public boolean triggerAlarm(ThreadTaskMetric metric, ThreadPoolAlarmConfig alarmConfig) {
        AtomicInteger counter = threadPoolTaskCounter.get(metric.getPoolName());
        if (counter == null) {
            threadPoolTaskCounter.putIfAbsent(metric.getPoolName(), new AtomicInteger(0));
            counter = threadPoolTaskCounter.get(metric.getPoolName());
        }

        if (metric.getExecutionCost() >= alarmConfig.getExecThreshold()) {
            int count = counter.incrementAndGet();
            if (count >= alarmConfig.getTaskNums()) {
                counter.getAndAdd(-alarmConfig.getTaskNums());
                return true;
            }
        }

        return false;
    }
}
