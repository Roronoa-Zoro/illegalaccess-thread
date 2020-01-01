package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.metric.ThreadPoolMetric;
import com.illegalaccess.thread.sdk.metric.ThreadTaskMetric;
import com.illegalaccess.thread.sdk.support.ConfigUtil;
import com.illegalaccess.thread.sdk.support.MetricPipeline;
import com.illegalaccess.thread.sdk.support.TaskLifecycleSavepoint;
import com.illegalaccess.thread.sdk.support.ThreadPoolMetricPool;
import com.illegalaccess.thread.sdk.thread.TracedThreadPoolExecutor;
import com.illegalaccess.thread.sdk.thread.TracedThreadPoolManager;
import com.illegalaccess.thread.sdk.utils.SdkConstants;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiao on 2019/12/21.
 *
 * 从blockqing收取TaskLifecycleSavepoint，传输到 队列
 * 从队列收集数据，进行简单计算汇总，然后发送数据到新的队列
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

    private TracedThreadPoolManager tracedThreadPoolManager;

    public CollectMetricTask(TracedThreadPoolManager tracedThreadPoolManager) {
        this.tracedThreadPoolManager = tracedThreadPoolManager;
        ConfigUtil.loadProp();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            LocalDateTime afterCollectInterval = LocalDateTime.now().plusSeconds(ConfigUtil.COLLECT_INTERVAL);
            while (LocalDateTime.now().isBefore(afterCollectInterval)) { // 收集线程任务的耗时未超过15秒
                TaskLifecycleSavepoint savepoint = MetricPipeline.Instance.fetch(MetricPipeline.Instance.INNER_SAVE_POINT_QUEUE);
                if (savepoint == null) {
                    continue;
                }
                ThreadTaskMetric metric = new ThreadTaskMetric();
                metric.setPoolName(savepoint.getThreadPoolName());
                metric.setWaitingCost(savepoint.calculateWaitingTime());
                metric.setExecutionCost(savepoint.calculateExecTime());
                metric.setRejected(savepoint.isRejected());
                ThreadPoolMetricPool.Instance.addThreadTaskMetric(metric);
                MetricPipeline.Instance.emit(MetricPipeline.Instance.INNER_THREAD_METRIC_QUEUE, metric);
            }

            // todo 获取线程池的运行时数据
            Collection<ThreadPoolMetric> executors = tracedThreadPoolManager.pickUpThreadPoolMetric();
            executors.forEach(poolMetric -> {
                ThreadPoolMetricPool.Instance.addThreadPoolMetric(poolMetric);
            });

            collectTime.incrementAndGet();
            if (collectTime.get() % ConfigUtil.REPORT_CYCLE == 0) {
                // todo 上报数据到 meta-server
                MetricPipeline.Instance.emit(MetricPipeline.Instance.INNER_REPORT_TASK_QUEUE, ThreadPoolMetricPool.Instance.toReportBO());
            }
        }

    }
}
