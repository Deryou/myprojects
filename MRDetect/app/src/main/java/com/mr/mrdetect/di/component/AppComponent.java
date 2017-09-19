package com.mr.mrdetect.di.component;

import com.mr.mrdetect.gloable.MRApplication;

/**
 * Created by MR on 2017/9/12.
 */

//@Singleton
//@Component(modules = AppModule.class)
public interface AppComponent {
    //提供app的Context
    MRApplication getContext();
}
