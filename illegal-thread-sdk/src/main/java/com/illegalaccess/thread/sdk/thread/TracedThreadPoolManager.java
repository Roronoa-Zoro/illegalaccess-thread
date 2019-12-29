package com.illegalaccess.thread.sdk.thread;

import com.google.common.eventbus.Subscribe;
import com.illegalaccess.thread.sdk.task.CollectAlarmTask;
import com.illegalaccess.thread.sdk.task.CollectMetricTask;
import com.illegalaccess.thread.sdk.task.ReportMetricTask;
import com.illegalaccess.thread.sdk.utils.SdkConstants;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xiao on 2019/12/21.
 */
public class TracedThreadPoolManager {

    private CopyOnWriteArrayList<TracedThreadPoolExecutor> tracedThreadPoolExecutors = new CopyOnWriteArrayList<>();
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
        tracedThreadPoolExecutors.add(executor);
        startCollectTask();
    }

    // todo
    @Subscribe
    public void threadPoolConfigChangeEventListener(String str) {

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

    public CopyOnWriteArrayList<TracedThreadPoolExecutor> getTracedThreadPoolExecutors() {
        return tracedThreadPoolExecutors;
    }
}
