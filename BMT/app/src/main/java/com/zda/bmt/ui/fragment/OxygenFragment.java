package com.zda.bmt.ui.fragment;

import android.widget.EditText;

import com.zda.bmt.R;
import com.zda.bmt.callback.OxygenCallback;

import butterknife.BindView;

/**
 *
 */
public class OxygenFragment extends BaseFragment implements OxygenCallback {

    @BindView(R.id.checked_oxygen)
    EditText mCheckedOxygen;

    public OxygenFragment() {
        // Required empty public constructor
    }


    public static OxygenFragment newInstance() {
        OxygenFragment fragment = new OxygenFragment();
        return fragment;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_oxygen;
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
    public String getInputData() {
        return mCheckedOxygen == null ? "" : mCheckedOxygen.getText().toString();
    }

    @Override
    public String setDataName(int type) {
        return "Oa";
    }
}
