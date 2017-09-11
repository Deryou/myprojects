package com.zda.duonews.di.component;

import com.zda.duonews.app.App;
import com.zda.duonews.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MR on 2017/7/27.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    //提供App的Context
    App getContext();

}
