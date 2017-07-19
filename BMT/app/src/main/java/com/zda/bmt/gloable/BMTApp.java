package com.zda.bmt.gloable;

import android.app.Application;
import android.content.Context;

/**
 * Created by MR on 2017/7/17.
 */

public class BMTApp extends Application{
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
