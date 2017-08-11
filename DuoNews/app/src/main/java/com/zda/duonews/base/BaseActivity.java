package com.zda.duonews.base;

import com.zda.duonews.app.App;
import com.zda.duonews.di.component.ActivityComponent;
import com.zda.duonews.di.component.DaggerActivityComponent;

import javax.inject.Inject;

/**
 * Created by MR on 2017/7/27.
 */

public abstract class BaseActivity<T extends BasePresenter> extends BaseCommActivity implements
        BaseView{

    @Inject
    protected T mPresenter;

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder().build();
    }
}
