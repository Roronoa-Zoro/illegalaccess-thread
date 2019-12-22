package com.illegalaccess.thread.sdk.client;

import com.illegalaccess.thread.sdk.bo.ThreadPoolAlarmBO;
import com.illegalaccess.thread.sdk.bo.ThreadPoolConfigBO;
import com.illegalaccess.thread.sdk.bo.ThreadPoolReportBO;
import com.illegalaccess.thread.sdk.support.ConfigUtil;
import com.illegalaccess.thread.sdk.utils.JsonUtil;
import com.illegalaccess.thread.sdk.utils.SdkConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by xiao on 2019/12/20.
 * meta-server interface
 */
@Slf4j
public class MetaServiceClient {

    private MetaServiceClientDelegate delegate = new MetaServiceClientDelegate();

    public List<ThreadPoolConfigBO> reportThreadPoolMetric(ThreadPoolReportBO reportBO) {

        Optional<String> resp = safeHttpInvoke(ConfigUtil.META_SERVER + ConfigUtil.REPORT_THREAD_POOL_METRIC_URI, reportBO);
        String realData = resp.orElse("");
        if (SdkConstants.BLANK.equals(realData)) {
            return new ArrayList<>(0);
        }

        Optional<List<ThreadPoolConfigBO>> optional = JsonUtil.string2List(realData, ThreadPoolConfigBO.class);
        List<ThreadPoolConfigBO> data = optional.orElse(new ArrayList<>(0));

        // todo
        return data;
    }

    public Object reportThreadPoolAlarm(ThreadPoolAlarmBO alarmBO) {
        // todo
        return null;
    }

    public List<ThreadPoolConfigBO> fetchThreadPoolConfig() {
        // todo
        return null;
    }

    private <T> Optional<String> safeHttpInvoke(String fullUrl, T data) {
        try {
            return Optional.ofNullable(delegate.doPost(fullUrl, JsonUtil.object2String(data)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
