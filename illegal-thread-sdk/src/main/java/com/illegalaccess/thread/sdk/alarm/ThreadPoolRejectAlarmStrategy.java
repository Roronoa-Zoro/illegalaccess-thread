package com.illegalaccess.thread.sdk.alarm;


import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.thread.NamedThreadPoolExecutor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiao on 2019/12/28.
 * 通过任务的被拒绝次数进行报警
 */
public class ThreadPoolRejectAlarmStrategy implements ThreadPoolAlarmStrategy {
    private static final long serialVersionUID = -2715704871997116354L;

    private AtomicLong rejectCounter = new AtomicLong(0);

    @Override
    public boolean triggerAlarm(NamedThreadPoolExecutor executor, ThreadPoolAlarmConfig alarmConfig) {
        return false;
    }
}
