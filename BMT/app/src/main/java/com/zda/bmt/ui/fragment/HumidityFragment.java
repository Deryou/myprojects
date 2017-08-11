package com.zda.bmt.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zda.bmt.R;
import com.zda.bmt.callback.HumidtyCallback;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HumidityFragment extends BaseFragment implements HumidtyCallback{

    @BindView(R.id.checked_humidity)
    EditText mCheckedHumidity;

    public HumidityFragment() {
        // Required empty public constructor
    }

    public static HumidityFragment newInstance() {
        HumidityFragment fragment = new HumidityFragment();
        return fragment;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_humidity;
    }

    @Override
    public void setData(float sendData) {
        setChartData(sendData);
    }

    @Override
    public void clearData() {
        if (mLineChart != null && mLineChart.getData() != null) {
            mLineChart.clearValues();
        }
    }

    @Override
    public String setDataName(int type) {
        return "Ha";
    }

    @Override
    public String getInputData() {
        return mCheckedHumidity.getText() == null ? "" : mCheckedHumidity.getText().toString();
    }
}
