package com.illegalaccess.thread.sdk.task;

import com.illegalaccess.thread.sdk.bo.ThreadPoolConfig;
import com.illegalaccess.thread.sdk.bo.ThreadPoolReportReq;
import com.illegalaccess.thread.sdk.client.MetaClientFactory;
import com.illegalaccess.thread.sdk.support.MetricPipeline;
import com.illegalaccess.thread.sdk.support.NamedThreadPoolEventSource;
import com.illegalaccess.thread.sdk.utils.SdkConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by xiao on 2019/12/22.
 */
@Slf4j
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
            List<ThreadPoolConfig> configList = MetaClientFactory.getClient().reportThreadPoolMetric(reportBO);
            NamedThreadPoolEventSource.Instance.publishThreadPoolConfigChangeEvent(configList);
            System.out.println("report====" + reportBO);
        }
    }
}
