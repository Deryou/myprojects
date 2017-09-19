package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.UserInfo;

/**
 * Created by MR on 2017/9/4.
 */

public interface PersonInfoContract {
    interface View extends BaseRootView{
        void loadSuccess(UserInfo.UserData data);
    }

    interface Presenter extends BasePresenter {
        void getUserData();

        /**
         * 修改用户信息
         */
        void modifyData();
    }

}
