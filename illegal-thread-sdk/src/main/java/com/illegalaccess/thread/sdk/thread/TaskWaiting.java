package com.illegalaccess.thread.sdk.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xiao on 2019/12/20.
 */
public class TaskWaiting {

    private static ConcurrentMap<Object, Long> data = new ConcurrentHashMap<>();

    public static void putStartTime(Object task, Long start) {
        data.putIfAbsent(task.toString(), start);
        System.out.println("map put:key|" + task + ", value|" + start);
    }

    public static Long clearTask(Object task) {
        Long start = data.get(task.toString());
        data.remove(task);
        return start;
    }
    
    public static void taskEnQueue(Object task) {
        
    }
    
    public static void taskOutOfQueue(Object task) {
        
    }
    
    public static void taskStartExec(Object task) {
        
    }
    
    public static void taskEndExec(Object task) {
        
    }
}
