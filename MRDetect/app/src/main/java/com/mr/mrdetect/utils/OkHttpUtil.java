package com.mr.mrdetect.utils;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by MR on 2017/8/29.
 */

public class OkHttpUtil {
    //定义好的头部的媒体类型
    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    //响应成功
    private static final int RESPONCE_SUCCESS = 0x00;
    //响应失败，直接未连接至服务器
    private static final int RESPONCE_FAILED = 0x01;
    //响应错误，连接至服务器，带错误码
    private static final int RESPONCE_ERROR = 0x02;
    //默认的超时时间
    public static long TIME_OUT = 6000;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONCE_SUCCESS:
                    break;
                case RESPONCE_FAILED:
                    break;
                case RESPONCE_ERROR:
                    break;
            }
        }
    };
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    private static HttpUrl configureUrl(String baseUrl, String urlPath, boolean isSecurity,
                                        Map<String,
            String> params) {
        String scheme = "";
        if (isSecurity) {
            scheme = "https";
        } else {
            scheme = "http";
        }
        HttpUrl.Builder builder;
        if (baseUrl.contains("http://") || baseUrl.contains("https://")) {
            baseUrl = baseUrl.split("//")[1];
        }
        baseUrl = clearUrlNoUseMark(baseUrl);
        urlPath = clearUrlNoUseMark(urlPath);
        if (baseUrl.contains(":")) {
            String[] urlArray = baseUrl.split(":");
            builder = new HttpUrl.Builder().scheme(scheme).host(urlArray[0]).port(Integer.valueOf
                    (urlArray[1])).addPathSegment(urlPath);
        } else {
            builder = new HttpUrl.Builder().scheme(scheme).host(baseUrl).addPathSegment(urlPath);
        }
        if (params != null) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private static String clearUrlNoUseMark(String targetUrl) {
        char head;
        char tail;
        head = targetUrl.charAt(0);
        tail = targetUrl.charAt(targetUrl.length() - 1);
        if (tail == '/') {
            targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
        }
        if (head == '/') {
            targetUrl = targetUrl.substring(1, targetUrl.length() - 1);
        }
        return targetUrl;
    }

    public static void httpPostJson(String baseUrl, String urlPath, String jsonData, boolean
            isSecurity) throws IOException {
        HttpUrl httpUrl = configureUrl(baseUrl, urlPath, isSecurity, null);
        if (jsonData == null) {
            throw new RuntimeException("确保已传入json格式数据");
        }
        RequestBody body = RequestBody.create(TYPE_JSON, jsonData);
        Request.Builder builder = new Request.Builder().url(httpUrl).post(body);
//        builder = configureHeader(builder, headers);
        Request request = builder.build();
        final Message msg = handler.obtainMessage();
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                msg.what = RESPONCE_FAILED;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                msg.what = RESPONCE_SUCCESS;
            }
        });
    }

    private static Request.Builder configureHeader(Request.Builder builder, Map<String, String>
            headers) {
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }
}
