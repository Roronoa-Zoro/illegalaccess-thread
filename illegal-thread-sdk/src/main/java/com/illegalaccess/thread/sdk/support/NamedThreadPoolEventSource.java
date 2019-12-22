package com.illegalaccess.thread.sdk.support;

import com.google.common.eventbus.EventBus;
import com.illegalaccess.thread.sdk.thread.NamedThreadPoolExecutor;
import com.illegalaccess.thread.sdk.thread.NamedThreadPoolManager;


/**
 * Created by xiao on 2019/12/21.
 */
public enum NamedThreadPoolEventSource {

    Instance;

    private EventBus eventBus = new EventBus();

    private NamedThreadPoolManager eventSubscriber = new NamedThreadPoolManager();

    NamedThreadPoolEventSource() {
        eventBus.register(eventSubscriber);
    }


    public void publishNamedThreadPoolCreationEvent(NamedThreadPoolExecutor executor) {
        eventBus.post(executor);
    }

    // todo
    public void publishThreadPoolConfigChangeEvent() {

    }
}
