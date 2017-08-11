package com.zda.duonews.di.component;

import android.app.Activity;

import com.zda.duonews.di.module.ActivityModule;
import com.zda.duonews.di.scope.ActivityScope;
import com.zda.duonews.ui.activity.main.MainActivity;

import dagger.Component;

/**
 * Created by MR on 2017/7/27.
 */
@ActivityScope
@Component(dependencies = AppComponent.class,modules ={ActivityModule.class} )
public interface ActivityComponent {
    Activity getActivity();

    void inject(MainActivity mainActivity);
}
