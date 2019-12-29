package com.illegalaccess.thread.sdk.alarm;

import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.thread.TracedThreadPoolExecutor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiao on 2019/12/28.
 */
public class ThreadPoolTaskCostAlarmStrategy implements ThreadPoolAlarmStrategy {

    private AtomicLong overExecThresHoldCounter = new AtomicLong();

    @Override
    public boolean triggerAlarm(TracedThreadPoolExecutor executor, ThreadPoolAlarmConfig alarmConfig) {
        return false;
    }
}
