package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.BannerBean;

import java.util.List;

/**
 * Created by MR on 2017/9/11.
 */

public interface HomeContract {
    interface View extends BaseRootView {
        void showBanner(List<BannerBean.BannerData> data);
    }

    interface Presenter extends BasePresenter{
        void getBannerData();
    }
}
