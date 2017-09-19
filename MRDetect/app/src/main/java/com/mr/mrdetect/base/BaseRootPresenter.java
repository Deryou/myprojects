package com.mr.mrdetect.base;

import android.widget.Toast;

import com.mr.mrdetect.gloable.MRApplication;
import com.mr.mrdetect.utils.NetworkUtil;

/**
 * Created by MR on 2017/9/11.
 */

public abstract class BaseRootPresenter{

    protected void checkNetWork() {
        if (!NetworkUtil.isNetConnected(MRApplication.getAppContext())) {
            Toast.makeText(MRApplication.getAppContext(), "当前网络无连接", Toast.LENGTH_LONG).show();
        }
    }
}
