package com.mr.bst.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.bst.R;
import com.mr.bst.callback.ShakeCallback;

/**
 * Created by MR on 2017/6/17.
 */

public class TpShakeFragment extends BaseFragment implements ShakeCallback{

    public static TpShakeFragment newInstance() {
        TpShakeFragment fragment = new TpShakeFragment();
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.tp_linechart_fragment;
    }

    @Override
    public void setData(float sendData) {

    }

    @Override
    public String setDataName() {
        return "振动";
    }

    @Override
    public void clearData() {
        mLineChart.clearValues();
    }
}
