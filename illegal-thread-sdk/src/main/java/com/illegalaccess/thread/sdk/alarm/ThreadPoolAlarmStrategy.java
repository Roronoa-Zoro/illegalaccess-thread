package com.illegalaccess.thread.sdk.alarm;

import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import java.io.Serializable;

/**
 * Created by xiao on 2019/12/21.
 * 线程池报警信息
 */
public interface ThreadPoolAlarmStrategy extends Serializable {

    boolean triggerAlarm(ThreadTaskMetric metric, ThreadPoolAlarmConfig alarmConfig);
//    private String poolName;

}
