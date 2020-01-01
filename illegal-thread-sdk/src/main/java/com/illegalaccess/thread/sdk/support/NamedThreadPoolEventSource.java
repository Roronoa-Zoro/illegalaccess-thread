package com.illegalaccess.thread.sdk.support;

import com.google.common.eventbus.EventBus;
import com.illegalaccess.thread.sdk.bo.ThreadPoolConfig;
import com.illegalaccess.thread.sdk.thread.TracedThreadPoolExecutor;
import com.illegalaccess.thread.sdk.thread.TracedThreadPoolManager;

import java.util.List;


/**
 * Created by xiao on 2019/12/21.
 */
public enum NamedThreadPoolEventSource {

    Instance;

    private EventBus eventBus = new EventBus();

    private TracedThreadPoolManager eventSubscriber = new TracedThreadPoolManager();

    NamedThreadPoolEventSource() {
        eventBus.register(eventSubscriber);
    }


    public void publishNamedThreadPoolCreationEvent(TracedThreadPoolExecutor executor) {
        eventBus.post(executor);
    }

    // todo
    public void publishThreadPoolConfigChangeEvent(List<ThreadPoolConfig> threadPoolConfigList) {
        eventBus.post(threadPoolConfigList);
    }
}
