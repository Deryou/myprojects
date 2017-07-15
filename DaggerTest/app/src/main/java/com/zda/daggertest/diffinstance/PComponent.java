package com.zda.daggertest.diffinstance;

import com.zda.daggertest.MainActivity;

import dagger.Component;

/**
 * Created by MR on 2017/7/11.
 */

@Component(modules = {PModule.class})
public interface PComponent {
    void inject(MainActivity activity);
}
