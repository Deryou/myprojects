package com.mr.mrdetect.di.component;

import android.app.Activity;

import com.mr.mrdetect.di.module.ActivityModule;
import com.mr.mrdetect.di.scope.ActivityScope;
import com.mr.mrdetect.ui.activity.LoginActivity;
import com.mr.mrdetect.ui.activity.MainActivity;
import com.mr.mrdetect.ui.activity.PersonInfoActivity;


/**
 * Created by MR on 2017/9/12.
 */
@ActivityScope
//@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(PersonInfoActivity personInfoActivity);
}
