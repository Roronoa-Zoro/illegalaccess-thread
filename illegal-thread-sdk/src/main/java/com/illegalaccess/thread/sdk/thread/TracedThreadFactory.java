package com.illegalaccess.thread.sdk.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TracedThreadFactory implements ThreadFactory {

    private final String DEFAULT_THREAD_PREFIX = "illegal-thread";
    // 线程ID后缀
    private final AtomicInteger suffix = new AtomicInteger(0);
    // 线程名字前缀
    private String threadPrefix;

    public TracedThreadFactory() {
        this.threadPrefix = DEFAULT_THREAD_PREFIX;
    }

    public TracedThreadFactory(String threadPrefix) {
        this.threadPrefix = threadPrefix;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, threadPrefix + "-thread-" + suffix.incrementAndGet());
        t.setDaemon(true);
        t.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler());
        return t;
    }
}
