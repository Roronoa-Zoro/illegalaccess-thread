package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.bo.ThreadPoolReportBO;
import com.illegalaccess.thread.sdk.support.MetricPipeline;
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

            ThreadPoolReportBO reportBO = MetricPipeline.Instance.fetchThreadMetricReport();
            if (reportBO == null) {
                continue;
            }


        }
    }
}
