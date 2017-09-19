package com.mr.mrdetect.di.module;

import com.mr.mrdetect.gloable.MRApplication;

/**
 * Created by MR on 2017/9/12.
 */

//@Module
public class AppModule {
    private final MRApplication mApplication;

    public AppModule(MRApplication application) {
        this.mApplication = application;
    }

//    @Provides
//    @Singleton
    MRApplication provideApplicationContext() {
        return mApplication;
    }
}
