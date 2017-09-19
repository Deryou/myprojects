package com.mr.mrdetect.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mr.mrdetect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by MR on 2017/8/22.
 */

public abstract class BaseMainFragment extends SupportFragment {

    //再点一次退出程序事件设置
    private static final long WAIT_TIME = 2000L;
    protected Unbinder mUnbinder;
    private long TOUCH_TIME = 0;

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    protected boolean isInited = false;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        mView= inflater.inflate(getLayoutSource(), container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    public void initView(View view) {
    }

    public abstract int getLayoutSource();

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        isInited = true;
        initEventAndData();
    }

    public void initEventAndData() {

    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
