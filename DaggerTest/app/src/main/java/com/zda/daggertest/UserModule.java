package com.zda.daggertest;

import android.content.Context;
import android.util.Log;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MR on 2017/7/10.
 */
@Module
public class UserModule {

    private Context mContext;

    public UserModule(Context context) {
        this.mContext = context;
    }

//    @Provides
//    public User provideUserModule() {
//        return new User();
//    }

    @Provides
    public String provideUrl() {
        return "www.baidu.com";
    }

    @Provides
    public UserInfo provideUserInfo() {
        return new UserInfo(mContext);
    }

    @Provides
    public UserManager provideUserManager(User user,UserInfo info) {
        return  new UserManager(user,info);
    }
}
