package com.illegalaccess.thread.sdk.support;

import com.illegalaccess.thread.sdk.thread.TracedTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiao on 2019/12/20.
 */
public class TaskWaiting {

    private static ConcurrentMap<Long, TaskLifecycleSavepoint> data = new ConcurrentHashMap<>();
    private static AtomicLong taskId = new AtomicLong(0);


    /**
     * 任务进入队列
     * @param task
     * @param <T>
     */
    public static <T> void taskEnQueue(T task) {
        if (!needTrace(task)) {
            return;
        }
        TaskLifecycleSavepoint savepoint = new TaskLifecycleSavepoint();
        TracedTask tt = (TracedTask) task;
        tt.setTraceId(taskId.getAndIncrement());
        savepoint.setEnqueueTime(getCurrentTime());
        data.put(tt.getTraceId(), savepoint);
    }

    /**
     * 任务出队列
     * @param task
     * @param <T>
     */
    public static <T> void taskOutOfQueue(T task) {
        if (!needTrace(task)) {
            return;
        }
        TracedTask tt = (TracedTask) task;
        Long traceId = tt.getTraceId();
        TaskLifecycleSavepoint savepoint = data.get(traceId);
        savepoint.setOutOfQueueTime(getCurrentTime());
    }

    /**
     * 第一个任务不进队列
     * @param task
     */
    public static <T> void taskStartExec(T task) {
        if (!needTrace(task)) {
            return;
        }
        TracedTask tt = (TracedTask) task;
        if (tt.getTraceId() == null) {
            TaskLifecycleSavepoint savepoint = new TaskLifecycleSavepoint();
            tt.setTraceId(taskId.getAndIncrement());
            data.put(tt.getTraceId(), savepoint);
        }
        TaskLifecycleSavepoint savepoint = data.get(tt.getTraceId());
        savepoint.setStartExecTime(getCurrentTime());
    }

    /**
     * 任务执行完成
     * @param task
     * @param <T>
     */
    public static <T> void taskEndExec(T task) {
        if (!needTrace(task)) {
            return;
        }
        TracedTask tt = (TracedTask) task;
        TaskLifecycleSavepoint savepoint = data.get(tt.getTraceId());
        savepoint.setEndExecTime(getCurrentTime());
    }

    public static void taskRejected(Object task) {

    }


    private static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private static <T> boolean needTrace(T data) {
        return data instanceof TracedTask;
    }
}
