package com.mr.mrdetect.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.mr.mrdetect.R;
import com.mr.mrdetect.utils.SnackbarUtil;

/**
 * Created by MR on 2017/9/5.
 */

public abstract class RootFragment extends BaseMainFragment implements
        BaseRootView {

    private static final int STATE_SUCCESS = 0x00;
    private static final int STATE_LOADING = 0x01;
    private static final int STATE_ERROR = 0x02;
    //    @Inject
    private View viewError;
    private View viewLoading;
    private ViewGroup viewMain;
    private ViewGroup mParent;

//    protected FragmentComponent  getFragmentComponent() {
//return null;
//    }

    @Override
    public void initEventAndData() {
        if (getView() == null) {
            return;
        }
        viewMain = getView().findViewById(R.id.view_main);
        if (viewMain == null) {
            throw new IllegalStateException("RootFragment子类必须包含一个id为view_main的子视图控件");
        }
        if (!(viewMain.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException("view_main的父视图必须为ViewGroup");
        }
        mParent = (ViewGroup) viewMain.getParent();
        //设置加载视图

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        SnackbarUtil.showLong((ViewGroup) getActivity().findViewById(android.R.id.content), msg);
    }
}
