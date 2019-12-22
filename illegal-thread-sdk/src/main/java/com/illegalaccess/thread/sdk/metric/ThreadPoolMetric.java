package com.illegalaccess.thread.sdk.metric;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by xiao on 2019/12/20.
 * metric of thread pool
 */
@Setter
@Getter
@ToString
public class ThreadPoolMetric {

    public ThreadPoolMetric() {
        collectTime = LocalDateTime.now();
    }
    // 线程池名称
    private String poolName;
    // 核心线程数量
    private int coreCnt;
    // 活跃线程数量
    private int activeCnt;
    // 当前线程池里的线程数量
    private int currentCnt;
    // 最大线程数量
    private int maxCnt;
    // 等待执行的任务的数量
    private int pendingTaskCnt;

    private LocalDateTime collectTime;
}
