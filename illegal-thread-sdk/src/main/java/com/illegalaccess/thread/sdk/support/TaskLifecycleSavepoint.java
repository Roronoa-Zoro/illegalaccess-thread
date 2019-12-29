package com.illegalaccess.thread.sdk.support;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xiao on 2019/12/28.
 * 记录一个任务各个阶段发生的时间点
 */
@Setter
@Getter
public class TaskLifecycleSavepoint {

    private String threadPoolName;

    // 进入队列的时间
    private long enqueueTime;
    // 出队列的时间
    private long outOfQueueTime;
    // 开始执行的时间
    private long startExecTime;
    // 结束执行的时间
    private long endExecTime;

    private boolean rejected = false;

    /**
     * 计算任务在队列里面等待的时间
     *
     */
    public long calculateWaitingTime() {
        return outOfQueueTime - enqueueTime;
    }

    /**
     * 计算任务出队列后，到开始执行前 的等待调度的时间
     *
     */
    public long calculateDispatchTime() {
        return startExecTime - outOfQueueTime;
    }

    /**
     * 计算执行的的时间
     *
     */
    public long calculateExecTime() {
        return endExecTime - startExecTime;
    }
}
