package com.mr.mrdetect.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.mr.mrdetect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by MR on 2017/9/2.
 */

public abstract class BaseActivity extends SupportActivity{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceId());
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initData();
        mToolbar.setTitle(setToolbarTitle());
    }

    protected void initToolbar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager
                    .LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (mToolbar != null) {
            mToolbar.setTitle(setToolbarTitle());
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


    public void initData() {

    }

    public String setToolbarTitle() {
        return null;
    }

    public void initView() {

    }

    public abstract int getResourceId();
}
