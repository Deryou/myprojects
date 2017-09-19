package com.mr.mrdetect.mvp.constract;

import com.mr.mrdetect.base.BasePresenter;
import com.mr.mrdetect.base.BaseRootView;
import com.mr.mrdetect.mvp.module.bean.ReportBean;

/**
 * Created by MR on 2017/9/7.
 */

public interface ReportContract {
    interface View extends BaseRootView {
        void showContent(ReportBean data);
    }

    interface Presenter extends BasePresenter{
        void getReportData();
    }
}
