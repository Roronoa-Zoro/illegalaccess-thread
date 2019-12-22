package com.illegalaccess.thread.springboot.sample.controller;

import com.illegalaccess.thread.springboot.sample.domain.Sample;
import com.illegalaccess.thread.springboot.sample.domain.SampleReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by xiao on 2019/12/22.
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    Logger logger = LoggerFactory.getLogger(SampleController.class);

    @PostMapping(value = "/getSample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Sample> getSample(@RequestBody SampleReq req) {
        logger.info("sampleReq====={}", req);
        List<Sample> samples = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Sample sample = new Sample();
            sample.setName(UUID.randomUUID().toString());
            samples.add(sample);
        }
        return samples;
    }
}
