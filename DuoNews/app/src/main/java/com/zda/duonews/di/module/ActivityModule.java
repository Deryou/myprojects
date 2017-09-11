package com.zda.duonews.di.module;

import android.app.Activity;

import com.zda.duonews.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MR on 2017/7/27.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @ActivityScope
    @Provides
    public Activity provideActivity() {
        return mActivity;
    }
}
