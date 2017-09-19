package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;

/**
 * Created by MR on 2017/9/16.
 * 添加控制器Contract
 */

public interface AddControlerContract {
    interface View extends BaseRootView {
        void addSuccess();
    }

    interface Presenter extends BasePresenter {
        void getProductType();

        void addProduct(String categoryId, String equipDesc, String equipGUID, boolean isValue);
    }
}
