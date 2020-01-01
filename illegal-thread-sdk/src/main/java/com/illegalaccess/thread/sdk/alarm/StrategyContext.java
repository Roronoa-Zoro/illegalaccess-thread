package com.illegalaccess.thread.sdk.alarm;

import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xiao on 2020/1/1.
 */
@Slf4j
public enum StrategyContext {

    Instance;

    private ConcurrentMap<ThreadPoolAlarmTypeEnum, ThreadPoolAlarmStrategy> strategyHolder = new ConcurrentHashMap<>();
    StrategyContext() {
        strategyHolder.put(ThreadPoolAlarmTypeEnum.Reject_Strategy, new ThreadPoolRejectAlarmStrategy());
        strategyHolder.put(ThreadPoolAlarmTypeEnum.Task_Cost_Strategy, new ThreadPoolTaskCostAlarmStrategy());
    }

    public boolean dispatch(ThreadTaskMetric metric, ThreadPoolAlarmConfig alarmConfig) {
        ThreadPoolAlarmTypeEnum enums = ThreadPoolAlarmTypeEnum.getEnumByCode(alarmConfig.getAlarmConfigType());
        if (enums == null) {
            log.error("alarm type:{} is not invalid", alarmConfig.getAlarmConfigType());
            return false;
        }
        ThreadPoolAlarmStrategy alarmStrategy = strategyHolder.get(enums);
        return alarmStrategy.triggerAlarm(metric, alarmConfig);
    }
}
