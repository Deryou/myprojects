package com.mr.mrdetect.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MR on 2017/8/30.
 */

public class Util {

    /**
     * 判断字符串是否为空
     * @param data
     * @return
     */
    public static boolean isEmpty(String data) {
        return TextUtils.isEmpty(data)||data.equals("");
    }

    /**
     * 时间戳转换为标准时间
     * @param time
     * @return
     */
    public static String getDateTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long date = Long.valueOf(time);
        String dateTime = dateFormat.format(new Date(date * 1000L));
        return dateTime;
    }
}
