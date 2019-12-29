package com.illegalaccess.thread.sdk.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiao on 2019/12/20.
 */
public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(DefaultUncaughtExceptionHandler.class);

    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        logger.error("there is an exception for thread name:{}", t.getName(), e);
    }
}
