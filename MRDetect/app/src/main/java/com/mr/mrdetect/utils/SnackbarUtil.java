package com.mr.mrdetect.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by MR on 2017/9/6.
 *
 * 提示工具类
 */

public class SnackbarUtil {

    public static void showLong(View view, String msg) {
        Snackbar.make(view,msg,Snackbar.LENGTH_LONG).show();
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }
}
