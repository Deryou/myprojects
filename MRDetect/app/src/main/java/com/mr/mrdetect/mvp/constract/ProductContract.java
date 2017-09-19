package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.ProductBean;

/**
 * Created by MR on 2017/9/6.
 */

public interface ProductContract {
    interface View extends BaseRootView{
        void showContent(ProductBean bean);
    }

    interface Presenter extends BasePresenter {
        void getProductData();
    }
}
