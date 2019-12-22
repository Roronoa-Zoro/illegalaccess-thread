package com.illegalaccess.thread.springboot.sample.domain;

/**
 * Created by xiao on 2019/12/22.
 */
public class SampleReq {

    private String reqTime;

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    @Override
    public String toString() {
        return "SampleReq{" +
                "reqTime='" + reqTime + '\'' +
                '}';
    }
}
