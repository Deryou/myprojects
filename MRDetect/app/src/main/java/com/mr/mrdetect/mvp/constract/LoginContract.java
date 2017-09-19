package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;

/**
 * Created by MR on 2017/8/29.
 */

public interface LoginContract {

    interface View extends BaseRootView {
        /**
         * 登录成功处理
         */
        void loginSuccess();

        /**
         * 清空用户输入的信息
         */
        void clearUserInfo();
    }

    interface Presenter extends BasePresenter {
        /**
         * 登录处理
         */
        void loginDeal(String username,String password);
    }
}
