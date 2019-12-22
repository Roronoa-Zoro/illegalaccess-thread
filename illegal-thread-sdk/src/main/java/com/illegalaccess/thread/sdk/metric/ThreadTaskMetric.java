package com.illegalaccess.thread.sdk.metric;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by xiao on 2019/12/21.
 * 在线程池里面执行的任务的数据指标
 */
@Setter
@Getter
@ToString
public class ThreadTaskMetric {

    public ThreadTaskMetric() {
        collectTime = LocalDateTime.now();
    }

    private String poolName;
    // 在阻塞队列里面等待执行花费的时间，单位 毫秒
    private int waitingCost;
    // 任务执行的耗时，单位毫秒
    private int executionCost;

    private LocalDateTime collectTime;
}
