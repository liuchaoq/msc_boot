package com.msc.common.util.bps;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpCommon {
    /**
     * 发送post请求
     * @param url
     * @param formBody
     * @return
     */
    public static Response sendPostRequest(String url, FormBody formBody ) {
        Response response =null;
        String requestUrl = BPSBootUtil.getHttpQueryUrl(url);
        System.out.println(requestUrl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(formBody)
                .addHeader("Content-Type", "application/json").build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
    /**
     * 发送get请求
     * @param url
     * @param paramMap
     * @return
     */
    public static Response sendGetRequest(String url, Map<String, Object> paramMap ) {
        Response response =null;
        String requestUrl = BPSBootUtil.buildGetHttpRequestUrl(url, paramMap);
        System.out.println(requestUrl);
        OkHttpClient client = getHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("Content-Type", "application/json").build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    public static OkHttpClient getHttpClient(){
        OkHttpClient httpClient =new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(25, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//设置写超时时间
                .build();

        return httpClient;
    }
}
