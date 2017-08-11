package com.zda.duonews.base;

/**
 * Created by MR on 2017/7/27.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T View);

    void detachView();
}
