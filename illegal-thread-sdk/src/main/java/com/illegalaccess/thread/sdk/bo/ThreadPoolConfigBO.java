package com.illegalaccess.thread.sdk.bo;

import lombok.Data;

/**
 * Created by xiao on 2019/12/21.
 * 线程池的配置信息
 */
@Data
public class ThreadPoolConfigBO {

    private String poolName;
    // 核心线程数
    private int coreSize;
    // 最大线程数
    private int maxSize;
    // 线程保持活跃的等待时间，单位秒
    private int keepAliveTime;
    // 阻塞队列的长度
    private int queueLength;
}
