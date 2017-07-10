package com.mr.bst.ui.fragment;

import com.mr.bst.R;
import com.mr.bst.callback.TempCallback;
import com.mr.bst.gloable.AppConstant;

/**
 * Created by MR on 2017/6/17.
 */

public class TpTempFragment extends BaseFragment implements TempCallback {

    public static TpTempFragment newInstance() {
        TpTempFragment fragment = new TpTempFragment();
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.tp_linechart_fragment;
    }

    @Override
    public void setData(float[] sendData) {
        setChartData(sendData[AppConstant.TP_TEMP]);
    }

    @Override
    public String setDataName() {
        return "温度";
    }

    @Override
    public void clearData() {
        if (mLineChart != null && mLineChart.getData() != null) {
            mLineChart.clearValues();
        }
    }
}
