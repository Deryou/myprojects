package com.zda.duonews.di.module;

import com.zda.duonews.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MR on 2017/7/27.
 */
@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Singleton
    @Provides
    App provideApplicationContext() {
        return application;
    }
}
