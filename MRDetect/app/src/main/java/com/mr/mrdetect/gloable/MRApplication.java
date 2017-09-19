package com.mr.mrdetect.gloable;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by MR on 2017/8/31.
 */

public class MRApplication extends Application {
    private static MRApplication instance;
//    public static AppComponent appComponent;

    public static MRApplication getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        instance = this;
    }

//    public static AppComponent getAppComponent() {
//        if (appComponent == null) {
//            appComponent = DaggerAppComponent.builder()
//                    .appModule(new AppModule(instance))
//                    .build();
//        }
//        return appComponent;
//    }
}
