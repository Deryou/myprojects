package com.zda.duonews.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.zda.duonews.di.component.AppComponent;
import com.zda.duonews.di.component.DaggerAppComponent;
import com.zda.duonews.di.module.AppModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by MR on 2017/7/26.
 */

public class App extends Application{
    private static App instance;
    public static AppComponent appComponent;
    private Set<Activity> mActivities;

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void addActivity(Activity activity) {
        if (mActivities == null) {
            mActivities = new HashSet<>();
        }
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mActivities != null) {
            mActivities.remove(activity);
        }
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        if (mActivities != null) {
            synchronized (mActivities) {
                if (mActivities != null) {
                    for (Activity activity : mActivities) {
                        activity.finish();
                    }
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static AppComponent getAppcomponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .build();
        }
        return appComponent;
    }
}
