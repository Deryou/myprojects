package com.mr.detector.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mr.detector.R;
import com.mr.detector.callback.HumidtyCallback;
import com.mr.detector.gloable.AppConstants;
import com.mr.detector.ui.activity.EquipmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 湿度显示界面
 */
public class HumidityFragment extends BaseFragment implements HumidtyCallback {
    @BindView(R.id.checked_humidity)
    EditText mCheckedHum;
    private Map<String, String> humMap;

    public static HumidityFragment newInstance() {
        HumidityFragment fragment = new HumidityFragment();
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
        return R.layout.fragment_humidity;
    }

    @Override
    public void setData(float[] sendData, String tempType) {

        if (mLineChart != null) {
            mLineData = mLineChart.getData();
            if (mLineData != null && mLineData.getDataSetCount() > 0) {
                mLineData.addEntry(new Entry(EquipmentActivity.timeCount, sendData[AppConstants
                        .EQUIP_TH]), 0);
                mLineData.notifyDataChanged();
            } else {
                entries = new ArrayList<>();
                entries.add(new Entry(EquipmentActivity.timeCount, sendData[AppConstants
                        .EQUIP_TH]));
                dataSet = new LineDataSet(entries, "TH");
//                dataSet.setValues(EquipmentActivity.mEntries.get(AppConstants.EQUIP_TH));
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

            if (EquipmentActivity.isStart && tempType.equals(AppConstants.TYPE_32)) {
                if (humMap == null) {
                    humMap = new HashMap<>();
                }
                if (dataCount <= AppConstants.HUMIDITY_NUM) {
                    if (dataCount < 10) {
                        humMap.put("TH0" + dataCount, String.valueOf(sendData[AppConstants
                                .EQUIP_TH]));
                        humMap.put("SH0" + dataCount, mCheckedHum.getText().toString());
                    } else {
                        humMap.put("TH" + dataCount, String.valueOf(sendData[AppConstants
                                .EQUIP_TH]));
                        humMap.put("SH" + dataCount, mCheckedHum.getText().toString());
                    }
                }
                dataCount++;
                if (dataCount > 15) {
                    dataCount = 1;
                    humMap.clear();
//                    mPromptDialog.showSuccess("湿度数据采集完毕！");
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
    public Map<String, String> getHumData() {
        return humMap;
    }
}
