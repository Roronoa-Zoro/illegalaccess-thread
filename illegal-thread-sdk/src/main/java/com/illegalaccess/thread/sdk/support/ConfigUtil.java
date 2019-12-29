package com.illegalaccess.thread.sdk.support;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xiao on 2019/12/21.
 * 从META-INF/illegal.properties里面获取配置信息
 * illegal.appKey=XXXX
 * illegal.metaServer=XXXX
 */
@Slf4j
public class ConfigUtil {

    public static final String REPORT_THREAD_POOL_METRIC_URI = "/api/v1/meta/transferThreadMetricReport" ;

    public static final String REPORT_THREAD_POOL_ALARM_URI  = "/api/v1/meta/reportThreadPoolAlarm"  ;

    public static final String FETCH_THREAD_POOL_CONFIG      = "/api/v1/meta/fetchThreadPoolConfig"  ;

    public static String APP_KEY                                                                     ;
    public static String META_SERVER                                                                 ;
    // 收集线程池运行时数据的间隔，单位秒
    public static int COLLECT_INTERVAL                                                               ;
    // 收集线程池运行时数据的 最小时间间隔，5秒
    public static int MIN_COLLECT_INTERVAL                   = 5                                     ;
    public static int DEFAULT_COLLECT_INTERVAL               = 15                                     ;

    // 数据收集几次之后进行上报
    public static int REPORT_CYCLE                                                                   ;
    public static int MIN_REPORT_CYCLE                       = 4                                     ;
    public static int DEFAULT_REPORT_CYCLE                   = 4                                     ;

    private static AtomicBoolean propLoad = new AtomicBoolean(false);
    private static Properties props = new Properties();

    public static void loadProp() {
        if (propLoad.get()) {
            return;
        }

        if (propLoad.compareAndSet(false, true)) {
            String file = "/META-INF/illegal.properties";
            URL fileURL = ConfigUtil.class.getResource(file);
            if (fileURL != null) {
                try {
                    props.load(ConfigUtil.class.getResourceAsStream(file));
                    setAppKey(props.getProperty("illegal.appKey"));
                    setMetaServer(props.getProperty("illegal.metaServer"));
                    setCollectInterval(Integer.valueOf(props.getProperty("illegal.collectInterval", DEFAULT_COLLECT_INTERVAL + "")));
                    setReportCycle(Integer.valueOf(props.getProperty("illegal.reportCycle", DEFAULT_REPORT_CYCLE + "")));
                } catch (IOException e) {
                    log.error("load properties from file:{} fail", file, e);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setAppKey(String appKey) {
        APP_KEY = appKey;
    }

    public static void setMetaServer(String metaServer) {
        META_SERVER = metaServer;
    }

    public static void setCollectInterval(int collectInterval) {
        if (collectInterval < MIN_COLLECT_INTERVAL) {
            COLLECT_INTERVAL = MIN_COLLECT_INTERVAL;
            return;
        }

        COLLECT_INTERVAL = collectInterval;
    }

    public static void setReportCycle(int reportCycle) {
        if (reportCycle < MIN_REPORT_CYCLE) {
            REPORT_CYCLE = MIN_REPORT_CYCLE;
            return;
        }

        REPORT_CYCLE = reportCycle;
    }
}
