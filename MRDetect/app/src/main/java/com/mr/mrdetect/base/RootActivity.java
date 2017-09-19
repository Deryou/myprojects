package com.mr.mrdetect.base;

import com.mr.mrdetect.ui.activity.BaseActivity;
import com.mr.mrdetect.utils.SnackbarUtil;


/**
 * Created by MR on 2017/9/12.
 */

public abstract class RootActivity extends BaseActivity implements
        BaseRootView {

    //    @Inject

//    protected ActivityComponent getActivityComponent() {
//       return DaggerActivityComponent.builder()
//               .appComponent(MRApplication.getAppComponent())
//               .activityModule(getActivityModule())
//               .build();
//    }
//
//    protected ActivityModule getActivityModule() {
//        return new ActivityModule(this);
//    }

    @Override
    public void initData() {

    }

    @Override
    public void stateError() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void showMsg(String msg) {
        SnackbarUtil.showLong(this.findViewById(android.R.id.content), msg);
    }
}
