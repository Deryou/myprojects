package com.mr.mrdetect.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mr.mrdetect.gloable.MRApplication;

/**
 * Created by MR on 2017/9/4.
 *
 * 用于数据持久化处理
 */

public class DBUtil {

    //用户信息
    public static final String USER_INFO = "user_info";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String USER_GROUP = "user_group";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_MOBILE = "user_mobile";
    public static final String USER_REAL_NAME = "user_real_name";

    public static void saveUserInfo(String username,String userId,String groupId) {
        SharedPreferences sharedPreferences = MRApplication.getAppContext().getSharedPreferences
                (USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, username);
        editor.putString(USER_ID, userId);
        editor.putString(USER_GROUP, groupId);
        editor.apply();
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public static String getUserId() {
        SharedPreferences sharedPreferences = MRApplication.getAppContext().getSharedPreferences
                (USER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID,null);
    }
}
