package com.zda.duonews.base;

/**
 * Created by MR on 2017/7/27.
 */

public interface BaseView {

    void showErrorMsg(String msg);

    void useNightMode(boolean isNightMode);

    /***************   state    ****************/
    void stateError();

    void stateEmpty();

    void stateLoading();

    void stateMain();
}
