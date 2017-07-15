package com.zda.daggertest.diffinstance;

import android.util.Log;

import com.zda.daggertest.anotation.Li;
import com.zda.daggertest.anotation.Wang;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MR on 2017/7/11.
 */

@Module
public class PModule {
    private static final String TAG = "PModule";

    @Li
    @Provides
    public Person provideLi() {
        Person li = new Person();
        Log.e(TAG, "provideLi: "+li);
        return li;
    }

    @Wang
    @Provides
    public Person provideWang() {
        Person wang = new Person();
        Log.e(TAG, "provideWang: "+wang);
        return wang;
    }
}
