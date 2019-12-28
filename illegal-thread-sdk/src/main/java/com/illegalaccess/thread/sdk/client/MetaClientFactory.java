package com.illegalaccess.thread.sdk.client;

/**
 * Created by xiao on 2019/12/28.
 */
public class MetaClientFactory {
    private MetaClientFactory(){}

    private static MetaServiceClient client = new MetaServiceClient();

    public static MetaServiceClient getClient() {
        return client;
    }
}
