package com.illegalaccess.thread.sdk.alarm;

import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.thread.NamedThreadPoolExecutor;

import java.io.Serializable;

/**
 * Created by xiao on 2019/12/21.
 * 线程池报警信息
 */
public interface ThreadPoolAlarmStrategy extends Serializable {

    boolean triggerAlarm(NamedThreadPoolExecutor executor, ThreadPoolAlarmConfig alarmConfig);
//    private String poolName;

}
