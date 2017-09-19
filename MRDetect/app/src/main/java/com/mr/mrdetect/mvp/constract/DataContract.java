package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.DataBean;

/**
 * Created by MR on 2017/9/8.
 */

public interface DataContract {
    interface View extends BaseRootView {
        void showContent(DataBean dataBean);
    }

    interface Presenter extends BasePresenter {
        void getData();
    }
}
