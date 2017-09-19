package com.mr.mrdetect.di.module;

import android.app.Activity;

import com.mr.mrdetect.di.scope.ActivityScope;

/**
 * Created by MR on 2017/9/12.
 */

//@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    //    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
