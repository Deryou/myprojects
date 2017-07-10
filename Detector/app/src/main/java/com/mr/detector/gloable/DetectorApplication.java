package com.mr.detector.gloable;

import android.app.Application;
import android.content.Context;

/**
 * Created by MR on 2017/4/28.
 */

public class DetectorApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }
}
