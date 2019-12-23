package com.illegalaccess.thread.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illegalaccess.thread.sdk.support.ConfigUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xiao on 2019/12/22.
 */
public class MetaServiceClientDelegate {

    private ObjectMapper om = new ObjectMapper();

    private HttpURLConnection initConnection() {
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) urlConnection;
        connection.setRequestMethod("POST");//设定请求方式为"POST"，默认为"GET"
        connection.setDoOutput(true);//设置是否向HttpUrlConnction输出，因为这个是POST请求，参数要放在http正文内，因此需要设为true，默认情况下是false
        connection.setDoInput(true);//设置是否向HttpUrlConnection读入，默认情况下是true
        connection.setUseCaches(false);//POST请求不能使用缓存（POST不能被缓存）
        connection.setInstanceFollowRedirects(true);//设置只作用于当前的实例
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
        connection.setRequestProperty("appKey", ConfigUtil.APP_KEY);
        connection.setRequestProperty("holdTimeout", ConfigUtil.COLLECT_INTERVAL * ConfigUtil.REPORT_CYCLE + "");

        connection.setConnectTimeout(10 * 1000);//设置连接主机超时（单位：毫秒）
        connection.setReadTimeout((ConfigUtil.COLLECT_INTERVAL * ConfigUtil.REPORT_CYCLE) * 1000);   
        
        connection.connect();
        return connection;
    }
    
    
    private String readData(HttpURLConnection connection) {
        //对outputStream的写操作，又必须要在inputStream的读操作之前
        InputStream inputStream = connection.getInputStream();// <===注意，实际发送请求的代码段就在这里
//        IOUtils.read()
        //读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String lines;
        StringBuffer response = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            response.append(lines);
//            response.append("\r\n");
        }

        connection.disconnect();
        return response.toString();
    }
    
    public String doGet(String urlStr, String reqData) {
        HttpURLConnection connection = initConnection();
        // todo 
        String response = readData(connection);
        return response;
    }
    
    public String doPost(String urlStr, String reqData) throws IOException {
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) urlConnection;
        connection.setRequestMethod("POST");//设定请求方式为"POST"，默认为"GET"
        connection.setDoOutput(true);//设置是否向HttpUrlConnction输出，因为这个是POST请求，参数要放在http正文内，因此需要设为true，默认情况下是false
        connection.setDoInput(true);//设置是否向HttpUrlConnection读入，默认情况下是true
        connection.setUseCaches(false);//POST请求不能使用缓存（POST不能被缓存）
        connection.setInstanceFollowRedirects(true);//设置只作用于当前的实例
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
        connection.setRequestProperty("appKey", ConfigUtil.APP_KEY + "j");
        connection.setRequestProperty("holdTimeout", ConfigUtil.COLLECT_INTERVAL * ConfigUtil.REPORT_CYCLE + "");

        connection.setConnectTimeout(10 * 1000);//设置连接主机超时（单位：毫秒）
        connection.setReadTimeout((ConfigUtil.COLLECT_INTERVAL * ConfigUtil.REPORT_CYCLE) * 1000);

        connection.connect();
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        byte[] t = reqData.getBytes("utf-8");
        dataOutputStream.write(t);
        dataOutputStream.flush();
        dataOutputStream.close();

        //对outputStream的写操作，又必须要在inputStream的读操作之前
        InputStream inputStream = connection.getInputStream();// <===注意，实际发送请求的代码段就在这里
//        IOUtils.read()
        //读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String lines;
        StringBuffer response = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            response.append(lines);
//            response.append("\r\n");
        }

        connection.disconnect();
        return response.toString();

    }
}
