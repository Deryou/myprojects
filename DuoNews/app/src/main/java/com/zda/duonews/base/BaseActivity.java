package com.zda.duonews.base;

import android.view.ViewGroup;

import com.zda.duonews.app.App;
import com.zda.duonews.di.component.ActivityComponent;
import com.zda.duonews.di.component.DaggerActivityComponent;
import com.zda.duonews.di.module.ActivityModule;
import com.zda.duonews.util.SnackbarUtil;

import javax.inject.Inject;

/**
 * Created by MR on 2017/7/27.
 */

public abstract class BaseActivity<T extends BasePresenter> extends BaseCommActivity implements
        BaseView{

    @Inject
    protected T mPresenter;

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(App.getAppcomponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
        SnackbarUtil.show(((ViewGroup)findViewById(android.R.id.content)).getChildAt(0),msg);
    }

    protected abstract void initInject();
}
