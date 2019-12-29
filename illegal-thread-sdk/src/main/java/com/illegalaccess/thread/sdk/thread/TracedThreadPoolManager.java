package com.illegalaccess.thread.sdk.thread;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmConfig;
import com.illegalaccess.thread.sdk.bo.ThreadPoolConfig;
import com.illegalaccess.thread.sdk.task.CollectAlarmTask;
import com.illegalaccess.thread.sdk.task.CollectMetricTask;
import com.illegalaccess.thread.sdk.task.ReportMetricTask;
import com.illegalaccess.thread.sdk.utils.SdkConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xiao on 2019/12/21.
 */
public class TracedThreadPoolManager {

//    private CopyOnWriteArrayList<TracedThreadPoolExecutor> tracedThreadPoolExecutors = new CopyOnWriteArrayList<>();
    private ConcurrentMap<String, TracedThreadPoolExecutor> tracedThreadPoolExecutors = new ConcurrentHashMap<>();
    private ConcurrentMap<String, ThreadPoolConfig> threadPoolConfigMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, ThreadPoolAlarmConfig> threadPoolAlarmConfigMap = new ConcurrentHashMap<>();
    private AtomicBoolean taskStarted = new AtomicBoolean(false);
    private ExecutorService innerThreadPool;

    @Subscribe
    public void namedThreadPoolCreationEventListener(TracedThreadPoolExecutor arg) {
        // todo arg是NamedThreadPoolExecutor，把它加入到list， 然后启动线程任务
        /*
        任务1：收集任务等待和执行时间，每隔15秒收集 list里面的线程池的运行时数据
        任务2：启动报警监控任务，等待报警数据，然后调用meta-server
        任务3：异步调用meta-server数据上报接口
         */
        TracedThreadPoolExecutor executor = arg;
        tracedThreadPoolExecutors.put(executor.getThreadPoolName(), executor);
        startCollectTask();
    }

    // todo
    @Subscribe
    public void threadPoolConfigChangeEventListener(List<ThreadPoolConfig> threadPoolConfigs) {
        if (threadPoolConfigs == null || threadPoolConfigs.isEmpty()) {
            return;
        }


        for (ThreadPoolConfig threadPoolConfig : threadPoolConfigs) {
            ThreadPoolConfig localPoolConfig = threadPoolConfigMap.get(threadPoolConfig.getPoolName());
            if (localPoolConfig == null) { // 通过meta server配置的线程池，先触发的这个事件
                threadPoolConfigMap.put(threadPoolConfig.getPoolName(), threadPoolConfig);
                continue;
            }
            if (threadPoolConfig.getVersion() == localPoolConfig.getVersion()) {
                continue; // config is not changed
            }

            threadPoolConfigMap.put(threadPoolConfig.getPoolName(), threadPoolConfig);
            TracedThreadPoolExecutor executor = tracedThreadPoolExecutors.get(localPoolConfig.getPoolName());
            if (executor.getCorePoolSize() != threadPoolConfig.getCoreSize()) {
                executor.setCorePoolSize(threadPoolConfig.getCoreSize());
            }
            if (executor.getMaximumPoolSize() != threadPoolConfig.getMaxSize()) {
                executor.setMaximumPoolSize(threadPoolConfig.getMaxSize());
            }
            // todo 修改blocking queue容量


            ThreadPoolAlarmConfig localPoolAlarm = threadPoolAlarmConfigMap.get(threadPoolConfig.getPoolName());
            if (localPoolAlarm == null) { // 新增的
                threadPoolAlarmConfigMap.put(threadPoolConfig.getPoolName(), threadPoolConfig.getAlarmConfig());
                continue;
            }
            if (localPoolAlarm.getVersion() != threadPoolConfig.getAlarmConfig().getVersion()) {
                threadPoolAlarmConfigMap.put(threadPoolConfig.getPoolName(), threadPoolConfig.getAlarmConfig());
            }
        }


    }

    public void startCollectTask() {
        if (taskStarted.get()) {
            return;
        }

        if (taskStarted.compareAndSet(false, true)) {
            innerThreadPool = TracedExecutors.newThreadPoolExecutor(SdkConstants.INNER_THREAD_POOL_NAME, 3, 3, 5, TimeUnit.MINUTES, 1);
            innerThreadPool.submit(new CollectMetricTask(this));
            innerThreadPool.submit(new CollectAlarmTask());
            innerThreadPool.submit(new ReportMetricTask());
        }
    }

    public Collection<TracedThreadPoolExecutor> getTracedThreadPoolExecutors() {

        return tracedThreadPoolExecutors.values();
    }
}
