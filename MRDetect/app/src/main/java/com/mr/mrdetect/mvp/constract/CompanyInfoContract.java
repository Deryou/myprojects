package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.CompanyInfoBean;

import java.util.List;

/**
 * Created by MR on 2017/9/14.
 */

public interface CompanyInfoContract {
    interface View extends BaseRootView {
        void showCompanyInfo(CompanyInfoBean.CompanyData data);
    }

    interface Presenter extends BasePresenter {
        void getCompanyInfo();

        void modifyCompanyInfo();
    }
}
