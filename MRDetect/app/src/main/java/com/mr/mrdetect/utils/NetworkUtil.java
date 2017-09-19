package com.mr.mrdetect.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MR on 2017/9/4.
 */

public class NetworkUtil {

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
    }

    /**
     * 判断某一类型网络是否连接
     * @param context
     * @param type
     * @return
     */
    public static boolean isNetworkConnected(Context context, int type) {
        if (!isNetConnected(context)) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(type);
        if (networkInfo == null) {
            return false;
        } else {
            return networkInfo.isAvailable()&&networkInfo.isConnected();
        }
    }

    /**
     * 判断是否使用移动数据流量
     * @param context
     * @return
     */
    public static boolean isPhoneNetConnected(Context context) {
        int typeMobile = ConnectivityManager.TYPE_MOBILE;
        return isNetworkConnected(context, typeMobile);
    }

    /**
     * 判断是否在wifi环境下
     * @param context
     * @return
     */
    public static boolean isWifiNetConnected(Context context) {
        int typeWifi = ConnectivityManager.TYPE_WIFI;
        return isNetworkConnected(context, typeWifi);
    }
}
