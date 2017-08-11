package com.zda.bmt.ui.fragment;


import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.zda.bmt.R;
import com.zda.bmt.callback.TemperatureCallback;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends BaseFragment implements TemperatureCallback {
    private static final String TAG = "TemperatureFragment";

    @BindView(R.id.checked_temp)
    EditText mChedkedTemp;

    public static TemperatureFragment newInstance() {
        TemperatureFragment fragment = new TemperatureFragment();
        return fragment;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_temperature;
    }

    @Override
    public String setDataName(int type) {
        switch (type) {
            case 0:
                return "ST1";
            case 1:
                return "ST2";
            case 2:
                return "ST3";
            case 3:
                return "ST4";
            case 4:
                return "ST5";
        }
        return "";
    }

    @Override
    public void setData(float sendData,int type) {
        setChartData(sendData,type);
    }

    @Override
    public void clearData() {
        if (mLineChart != null && mLineChart.getData() != null) {
            mLineChart.clearValues();
        }
    }

    @Override
    public String getInputData() {
        return mChedkedTemp.getText() == null ? "" : mChedkedTemp.getText().toString();
    }
}
