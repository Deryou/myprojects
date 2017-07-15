package com.zda.daggertest.User;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by MR on 2017/7/10.
 */

public class User {

    private static final String TAG = "User";

//    @Inject
    public User() {

    }

    /**
     * 默认的只能有一个Inject
     * @param url
     */
    @Inject
    public User(String url) {
        Log.e(TAG, "User: ::::::"+url );
    }

    public void register() {
        Log.e(TAG, "register: 测试数据");
    }

    public void logTest() {
        Log.e(TAG, "logTest: 我到UserManager里面了");
    }
}
