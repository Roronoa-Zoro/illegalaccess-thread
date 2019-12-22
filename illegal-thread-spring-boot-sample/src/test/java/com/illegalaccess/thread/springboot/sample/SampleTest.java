package com.illegalaccess.thread.springboot.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illegalaccess.thread.sdk.client.MetaServiceClientDelegate;
import com.illegalaccess.thread.springboot.sample.domain.SampleReq;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiao on 2019/12/22.
 */
public class SampleTest {

    @Test
    public void postTest() throws IOException, InterruptedException {
        MetaServiceClientDelegate delegate = new MetaServiceClientDelegate();
        SampleReq req = new SampleReq();
        req.setReqTime(System.currentTimeMillis() + "HHH");
        ObjectMapper om = new ObjectMapper();
        String response = delegate.doPost("http://127.0.0.1:8080/sample/getSample", om.writeValueAsString(req));
        System.out.println("response====" + response);
        TimeUnit.SECONDS.sleep(5L);
    }

}
