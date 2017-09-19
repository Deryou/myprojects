package com.mr.mrdetect.base;

/**
 * Created by MR on 2017/9/6.
 */

public interface BaseRootView {
    /**
     * 显示正在加载
     */
    void stateLoading();

    void stateEmpty();

    void stateError();

    void showMsg(String msg);
}
