package com.illegalaccess.thread.sdk.utils;

/**
 * Created by xiao on 2019/12/20.
 */
public class SdkConstants {

    private SdkConstants() {
        throw new IllegalStateException("can not construct SdkConstants");
    }

    public static final String MINUS                         = "-"                                   ;
    public static final String BLANK                         = ""                                    ;
    public final static String EMPTY_JSON = "{}";

    public final static String INNER_THREAD_POOL_NAME = "innerTracedThreadPool";
}
