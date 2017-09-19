package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.ControlerBean;

/**
 * Created by MR on 2017/9/6.
 */

public interface ControlerContract {
    interface View extends BaseRootView {
        void showContent(ControlerBean bean);
    }

    interface Presenter extends BasePresenter {
        void getControlerData();
    }
}
