package com.zda.daggertest;

import dagger.Component;

/**
 * Created by MR on 2017/7/10.
 */

@Component(modules = {UserModule.class})
public interface UserComponent {
    void inject(MainActivity activity);
}
