package com.mr.detector.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mr.detector.R;
import com.mr.detector.callback.OxygenCallback;
import com.mr.detector.gloable.AppConstants;
import com.mr.detector.ui.activity.EquipmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OxygenFragment extends BaseFragment implements OxygenCallback {
    private static final String TAG = "OxygenFragment";

    @BindView(R.id.checked_oxygen)
    EditText mCheckedOxygen;
    private Map<String, String> oxygenMap;

    public static OxygenFragment newInstance() {
        OxygenFragment fragment = new OxygenFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_oxygen;
    }

    @Override
    public void setData(float[] sendData, String tempType) {

        if (mLineChart != null) {
            mLineData = mLineChart.getData();
            if (mLineData != null && mLineData.getDataSetCount() > 0) {
                mLineData.addEntry(new Entry(EquipmentActivity.timeCount, sendData[AppConstants
                        .EQUIP_TO]), 0);
                mLineData.notifyDataChanged();
            } else {
                entries = new ArrayList<>();
                entries.add(new Entry(EquipmentActivity.timeCount, sendData[AppConstants
                        .EQUIP_TO]));
                dataSet = new LineDataSet(entries, "TO");
//                dataSet.setValues(EquipmentActivity.mEntries.get(AppConstants.EQUIP_TO));
                dataSet.setLineWidth(2f);
                dataSet.setCircleRadius(4.5f);
                dataSet.setHighLightColor(Color.rgb(244, 117, 117));
                dataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
                dataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
                dataSet.setDrawValues(true);
                sets.add(dataSet);
                mLineData = new LineData(sets);
                mLineChart.setData(mLineData);
            }

            if (EquipmentActivity.isStart&& tempType.equals(AppConstants.TYPE_32)) {
                if (oxygenMap == null) {
                    oxygenMap = new HashMap<>();
                }
                if (dataCount <= AppConstants.OXYGEN_NUM) {
                    if (dataCount < 10) {
                        oxygenMap.put("TO0" + dataCount, String.valueOf(sendData[AppConstants
                                .EQUIP_TO]));
                        oxygenMap.put("SO0" + dataCount, mCheckedOxygen.getText().toString());
                    } else {
                        oxygenMap.put("TO" + dataCount, String.valueOf(sendData[AppConstants
                                .EQUIP_TO]));
                        oxygenMap.put("SO" + dataCount, mCheckedOxygen.getText().toString());
                    }
                }
                dataCount++;
                if (dataCount > 15) {
                        dataCount = 1;
//                    mPromptDialog.showSuccess("氧浓度数据采集完毕！");
                }
            }

            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        }
    }

    @Override
    public void clearData() {
        if (mLineData != null && mLineData.getDataSetCount() > 0) {
            mLineData.clearValues();
            mLineChart.invalidate();
        }
    }

    @Override
    public Map<String, String> getOxygendata() {
        return oxygenMap;
    }
}
