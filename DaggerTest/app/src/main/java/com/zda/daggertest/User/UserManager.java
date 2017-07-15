package com.zda.daggertest.User;

import android.util.Log;

/**
 * Created by MR on 2017/7/10.
 */

public class UserManager {
    private static final String TAG = "UserManager";
    private User mUser;
    public UserManager(User user) {
        mUser = user;
        user.logTest();
    }

    public UserManager(User user, UserInfo info) {
        this.mUser = user;
        user.logTest();
        info.theInfo();
    }
    public void register() {
        Log.e(TAG, "register: UserManager中的测试数据" );
        mUser.register();
    }
}
