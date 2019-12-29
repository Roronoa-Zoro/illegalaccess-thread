package com.illegalaccess.thread.sdk.support;

import com.illegalaccess.thread.sdk.utils.SdkConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiao on 2019/12/20.
 */
public class TaskLifecycleTracer {

//    private static ConcurrentMap<Long, TaskLifecycleSavepoint> data = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, TaskLifecycleSavepoint> data = new ConcurrentHashMap<>();
    private static AtomicLong taskId = new AtomicLong(0);
    private static ConcurrentMap<String, Integer> innerPoolNames;
    static {
        innerPoolNames = new ConcurrentHashMap<>(4);
        innerPoolNames.put(SdkConstants.INNER_THREAD_POOL_NAME, Integer.valueOf(1));
        innerPoolNames.put(MetricPipeline.Instance.INNER_SAVE_POINT_QUEUE, Integer.valueOf(1));
        innerPoolNames.put(MetricPipeline.Instance.INNER_THREAD_METRIC_QUEUE, Integer.valueOf(1));
        innerPoolNames.put(MetricPipeline.Instance.INNER_REPORT_TASK_QUEUE, Integer.valueOf(1));
    }


    /**
     * 任务进入队列
     * @param task
     * @param <T>
     */
    public static <T> void taskEnQueue(String threadPoolName, T task) {
        if (!needTrace(threadPoolName)) {
            return;
        }
        String key = createQniqueKey(threadPoolName, task.toString());
        TaskLifecycleSavepoint savepoint = new TaskLifecycleSavepoint();

//        TracedTask tt = (TracedTask) task;
//        tt.setTraceId(taskId.getAndIncrement());

        System.out.println(Thread.currentThread().getName() +" key=" + key + " is put");
        savepoint.setEnqueueTime(getCurrentTime());
        savepoint.setThreadPoolName(threadPoolName);
        data.put(key, savepoint);
    }

    /**
     * 任务出队列
     * @param task
     * @param <T>
     */
    public static <T> void taskOutOfQueue(String threadPoolName, T task) {
        if (!needTrace(threadPoolName)) {
            return;
        }
        String key = createQniqueKey(threadPoolName, task.toString());

//        TracedTask tt = (TracedTask) task;
//        Long traceId = tt.getTraceId();
        System.out.println("key>>>>" + key + " will be get");
        TaskLifecycleSavepoint savepoint = data.get(key);
        savepoint.setOutOfQueueTime(getCurrentTime());
    }

    /**
     * 第一个任务不进队列
     * @param task
     */
    public static <T> void taskStartExec(String threadPoolName, T task) {
        if (!needTrace(threadPoolName)) {
            return;
        }
        String key = createQniqueKey(threadPoolName, task.toString());
        TaskLifecycleSavepoint savepoint = data.get(key);
        if (savepoint == null) { // 有活跃线程时直接执行
            savepoint = new TaskLifecycleSavepoint();
            savepoint.setThreadPoolName(threadPoolName);
            data.put(key, savepoint);
        }
        savepoint.setStartExecTime(getCurrentTime());

//        TracedTask tt = (TracedTask) task;
//        if (tt.isTraced()) {
//            TaskLifecycleSavepoint savepoint = new TaskLifecycleSavepoint();
//            tt.setTraceId(taskId.getAndIncrement());
//            savepoint.setThreadPoolName(threadPoolName);
//            data.put(tt.getTraceId(), savepoint);
//        }
//        TaskLifecycleSavepoint savepoint = data.get(tt.getTraceId());
//        savepoint.setStartExecTime(getCurrentTime());
    }

    /**
     * 任务执行完成
     * @param task
     * @param <T>
     */
    public static <T> void taskEndExec(String threadPoolName, T task) {
        if (!needTrace(threadPoolName)) {
            return;
        }
        String key = createQniqueKey(threadPoolName, task.toString());
        TaskLifecycleSavepoint savepoint = data.remove(key);
        savepoint.setEndExecTime(getCurrentTime());
        MetricPipeline.Instance.emit(MetricPipeline.Instance.INNER_SAVE_POINT_QUEUE, savepoint);

//        TracedTask tt = (TracedTask) task;
//        TaskLifecycleSavepoint savepoint = data.remove(tt.getTraceId());
    }

    public static void taskRejected(String threadPoolName, Object task) {
        if (!needTrace(threadPoolName)) {
            return;
        }
        String key = createQniqueKey(threadPoolName, task.toString());
        TaskLifecycleSavepoint savepoint = data.remove(key);
        if (savepoint != null) { // 说明拒绝策略把已经在排队的任务干掉
            savepoint.setOutOfQueueTime(getCurrentTime());
            savepoint.setRejected(true);
        } else {
            savepoint = new TaskLifecycleSavepoint();
            savepoint.setRejected(true);
            savepoint.setThreadPoolName(threadPoolName);
        }

//        TracedTask tt = (TracedTask) task;
//        TaskLifecycleSavepoint savepoint = null;
//        if (tt.isTraced()) { // 说明拒绝策略把已经在排队的任务干掉六
//            data.remove(tt.getTraceId());
//            savepoint.setOutOfQueueTime(getCurrentTime());
//            savepoint.setRejected(true);
//        } else { //说明拒绝策略丢弃了新来的任务
//            savepoint = new TaskLifecycleSavepoint();
//            savepoint.setRejected(true);
//            savepoint.setThreadPoolName(threadPoolName);
//        }
        MetricPipeline.Instance.emit(MetricPipeline.Instance.INNER_SAVE_POINT_QUEUE, savepoint);

    }

    private static String createQniqueKey(String threadPoolName, String futureTaskString) {
        return threadPoolName + futureTaskString;
    }

    private static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private static boolean needTrace(String threadPoolName) {
        return (threadPoolName != null) && (innerPoolNames.get(threadPoolName) == null);
    }
}
