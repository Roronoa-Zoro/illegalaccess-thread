package com.illegalaccess.thread.sdk.bo;

import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xiao on 2019/12/28.
 * 线程池报警信息配置
 */
@ToString
@Builder
public class ThreadPoolAlarmReq implements Serializable {
    private static final long serialVersionUID = -9089406450663761038L;

    private String poolName;
}
