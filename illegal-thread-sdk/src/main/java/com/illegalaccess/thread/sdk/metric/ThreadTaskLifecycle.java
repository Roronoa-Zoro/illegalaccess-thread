package com.illegalaccess.thread.sdk.metric;

public class ThreadTaskLifecycle {

  // 进入队列的时间
  private long enqueueTime;
  // 出队列的时间
  private long outOfQueueTime;
  // 开始执行的时间
  private long startExecTime;
  // 结束执行的时间
  private long endExecTime;
  
  /**
  * 计算任务在队列里面等待的时间
  *
  */
  public long calculateEnQueueTime() {
      return outOfQueueTime - enqueueTime;
  }
  
  /**
  * 计算任务出队列后，到开始执行前 的等待调度的时间
  *
  */
  public long calculateDispatchTime() {
      return startExecTime - outOfQueueTime;
  }
 
  /**
  * 计算执行的的时间
  *
  */
  public long calculateExecTime() {
      return endExecTime - startExecTime;
  }

}
