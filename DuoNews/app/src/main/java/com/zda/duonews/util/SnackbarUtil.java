package com.zda.duonews.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by MR on 2017/8/17.
 */

public class SnackbarUtil {

    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
    }
}
