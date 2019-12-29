package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.bo.ThreadPoolReportReq;
import com.illegalaccess.thread.sdk.support.MetricPipeline;
import com.illegalaccess.thread.sdk.utils.SdkConstants;
import lombok.SneakyThrows;

/**
 * Created by xiao on 2019/12/22.
 */
public class ReportMetricTask implements Runnable {

    // todo MetaServiceClient

    @SneakyThrows
    @Override
    public void run() {
        // todo, MetaServiceClient 调用 meta-server 进行数据上报，拿回来数据就 通知 NamedThreadPoolManager进行更新
        while (!Thread.currentThread().isInterrupted()) {

            ThreadPoolReportReq reportBO = MetricPipeline.Instance.fetch(MetricPipeline.Instance.INNER_REPORT_TASK_QUEUE);
            if (reportBO == null) {
                continue;
            }
            System.out.println("report====" + reportBO);

        }
    }
}
