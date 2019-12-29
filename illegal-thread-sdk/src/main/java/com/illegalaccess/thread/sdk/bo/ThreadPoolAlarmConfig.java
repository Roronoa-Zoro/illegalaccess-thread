package com.illegalaccess.thread.sdk.bo;

import java.io.Serializable;

/**
 * Created by xiao on 2019/12/28.
 * 线程池报警配置
 * 1.按拒绝次数报警，连续发生N次拒绝任务，则报警
 * 2 按执行时间报警，有N次任务执行时间超过M毫秒，则报警
 */
public class ThreadPoolAlarmConfig implements Serializable {
    private static final long serialVersionUID = -9089406450663761038L;

    /**
     * @see com.illegalaccess.thread.sdk.alarm.ThreadPoolAlarmTypeEnum
     */
    private int alarmConfigType;

    /**
     * 当 alarmConfigType = Reject_Strategy 生效
     * 拒绝次数
     */
    private int rejectNums;


    /**
     * 当 alarmConfigType = Task_Cost_Strategy 生效
     */
    private int taskNums;
    /**
     * 当 alarmConfigType = Task_Cost_Strategy 生效
     */
    private int execThreshold;
}