package com.mr.bst.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.bst.R;
import com.mr.bst.callback.PressCallback;
import com.mr.bst.gloable.AppConstant;

import java.util.stream.Stream;

/**
 * Created by MR on 2017/6/17.
 */

public class TpPressureFragment extends BaseFragment implements PressCallback{

    public static TpPressureFragment newInstance() {
        TpPressureFragment fragment = new TpPressureFragment();
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.tp_linechart_fragment;
    }

    @Override
    public void setData(float[] sendData) {
        setChartData(sendData[AppConstant.TP_PRESSURE]);
    }

    @Override
    public String setDataName() {
        return "负压";
    }

    @Override
    public void clearData() {
        if (mLineChart != null && mLineChart.getData() != null) {
            mLineChart.clearValues();
        }
    }
}
