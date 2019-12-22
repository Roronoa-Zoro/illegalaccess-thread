package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.metric.ThreadPoolMetric;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import com.illegalaccess.thread.sdk.support.ConfigUtil;
import com.illegalaccess.thread.sdk.support.MetricPipeline;
import com.illegalaccess.thread.sdk.support.ThreadPoolMetricCollector;
import com.illegalaccess.thread.sdk.thread.NamedThreadPoolExecutor;
import com.illegalaccess.thread.sdk.thread.NamedThreadPoolManager;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiao on 2019/12/21.
 *
 * 从blockqing收取ThreadTaskMetric，传输到一个 待上报数据池 中
 *
 * 每隔 ConfigUtil.COLLECT_INTERVAL 秒，检查所有的线程池的运行时情况，输入到一个 待上报数据池 中
 *
 * 每收集ConfigUtil.REPORT_CYCLE 次 ，上报一次数据，
 *
 */
public class CollectMetricTask implements Runnable {

    // todo 从blockqing收取ThreadTaskMetric，输入到一个 待上报数据池 中

    // todo，每隔15秒，检查所有的线程池的运行时情况，输入到一个 待上报数据池 中

    // todo, 每隔60秒，上报一次数据，60s这个时间必须是第二个todo的时间的整数倍，

    private final AtomicInteger collectTime = new AtomicInteger(0);

    private NamedThreadPoolManager namedThreadPoolManager;

    public CollectMetricTask(NamedThreadPoolManager namedThreadPoolManager) {
        this.namedThreadPoolManager = namedThreadPoolManager;
        ConfigUtil.loadProp();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            LocalDateTime after15s = LocalDateTime.now().plusSeconds(ConfigUtil.COLLECT_INTERVAL);
            while (LocalDateTime.now().isBefore(after15s)) { // 收集线程任务的耗时未超过15秒
                ThreadTaskMetric metric = MetricPipeline.Instance.fetchThreadTaskMetric();
                if (metric == null) {
                    continue;
                }

                ThreadPoolMetricCollector.Instance.addThreadTaskMetric(metric);
            }

            // todo 获取线程池的运行时数据
            CopyOnWriteArrayList<NamedThreadPoolExecutor> executors = namedThreadPoolManager.getNamedThreadPoolExecutors();
            executors.forEach(executor -> {
                ThreadPoolMetric poolMetric = new ThreadPoolMetric();
                poolMetric.setPoolName(executor.getThreadPoolName());
                poolMetric.setCoreCnt(executor.getCorePoolSize());
                poolMetric.setActiveCnt(executor.getActiveCount());
                poolMetric.setCurrentCnt(executor.getPoolSize());
                poolMetric.setMaxCnt(executor.getMaximumPoolSize());
                poolMetric.setPendingTaskCnt(executor.getQueue().size());
                ThreadPoolMetricCollector.Instance.addThreadPoolMetric(poolMetric);
            });

            collectTime.incrementAndGet();
            if (collectTime.get() % ConfigUtil.REPORT_CYCLE == 0) {
                // todo 上报数据到 meta-server
                MetricPipeline.Instance.transferThreadMetricReport(ThreadPoolMetricCollector.Instance.toReportBO());
            }
        }

    }
}
