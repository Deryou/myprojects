package com.mr.detector.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
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
import com.mr.detector.callback.BackPressedHandler;
import com.mr.detector.callback.TemperatureCallback;
import com.mr.detector.gloable.AppConstants;
import com.mr.detector.ui.activity.EquipmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 温度数据展示界面
 */
public class TemperatureFragment extends BaseFragment implements TemperatureCallback,
        BackPressedHandler {
    private static final String TAG = "TemperatureFragment";
    @BindView(R.id.checked_temp)
    EditText mCheckedTemp;
    private String timeGap;
    private String checkTemp;
    private Map<String, List<Map<String, String>>> mDataTemp;
    private List<Map<String, String>> mMapList;

    public static TemperatureFragment newInstance() {
        TemperatureFragment fragment = new TemperatureFragment();
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
        return R.layout.fragment_temperature;
    }

    @Override
    public void setData(float[] sendData, String tempType) {

        if (mLineChart != null) {
            mLineData = mLineChart.getData();
            if (mLineData != null && mLineData.getDataSetCount() > 0) {
                for (int i = 0; i < mLineData.getDataSetCount(); i++) {
                    mLineData.addEntry(new Entry(EquipmentActivity.timeCount, sendData[i]), i);
                    mLineData.notifyDataChanged();
                }
            } else {
                int a = 65;
                for (int i = 0; i < 5; i++) {
                    entries = new ArrayList<>();
                    entries.add(new Entry(EquipmentActivity.timeCount, sendData[i]));
                    dataSet = new LineDataSet(entries, "T" + (char) (a + i));
//                    dataSet.setValues(EquipmentActivity.mEntries.get(i));
                    dataSet.setLineWidth(2f);
                    dataSet.setCircleRadius(4.5f);
                    dataSet.setHighLightColor(Color.rgb(244, 117, 117));
                    dataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[i]);
                    dataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[i]);
                    dataSet.setDrawValues(true);
                    sets.add(dataSet);
                }
                mLineData = new LineData(sets);
                mLineChart.setData(mLineData);
            }

            if (EquipmentActivity.isStart) {
                //设备号 对应的ascii码
                int equipNum = 65;
                if (mDataTemp == null) {
                    mDataTemp = new HashMap<>();
                    List<Map<String, String>> temp_32_list = new ArrayList<>();
                    List<Map<String, String>> temp_36_list = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        temp_32_list.add(new HashMap<String, String>());
                        temp_36_list.add(new HashMap<String, String>());
                    }
                    mDataTemp.put(AppConstants.TYPE_32, temp_32_list);
                    mDataTemp.put(AppConstants.TYPE_36, temp_36_list);
                }
                //数据的添加：用户输入的对比数与实际检测的数值

                if (dataCount < 16) {
                    if (tempType.equals(AppConstants.TYPE_32) || tempType == null) {
                        mMapList = mDataTemp.get(AppConstants.TYPE_32);
                    } else if (tempType.equals(AppConstants.TYPE_36)) {
                        mMapList = mDataTemp.get(AppConstants.TYPE_36);
                    }
                    for (int i = 0; i < mMapList.size(); i++) {
                        if (i < 5) {
                            if (dataCount < 10) {
                                mMapList.get(i).put("T" + (char) (equipNum + i) + tempType +
                                                "0" + dataCount,
                                        String.valueOf(sendData[i]));
                            } else {
                                mMapList.get(i).put("T" + (char) (equipNum + i) + tempType +
                                                dataCount,
                                        String.valueOf(sendData[i]));
                            }
                        } else {
                            if (dataCount < 10) {
                                mMapList.get(i).put("ST" + tempType + "0" + dataCount,
                                        mCheckedTemp.getText().toString());
                            } else {
                                mMapList.get(i).put("ST" + tempType + dataCount, mCheckedTemp
                                        .getText().toString());
                            }
                        }
                    }
                }

                dataCount++;
                if (dataCount > 15) {
                    EquipmentActivity.isStart = false;
                    mResultCallback.onDataRecEnd();
                    dataCount = 1;

//                    mPromptDialog.showSuccess("数据采集完毕！");
                    Toast.makeText(getContext(), "温度数据采集完毕", Toast.LENGTH_SHORT).show();
                }
            }

            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapList != null) {
            mMapList = null;
        }
    }

    @Override
    public Map<String, List<Map<String, String>>> getTempReport() {
        return mDataTemp;
    }


    @Override
    public void dataClear() {
        for (int i = 0; i < mMapList.size(); i++) {
            mMapList.get(i).clear();
        }
    }

    @Override
    public void tempStateChanged(String data) {
        mCheckedTemp.setText(data);
    }

    @Override
    public void clearData() {
        if (mLineData != null && mLineData.getDataSetCount() > 0) {
            mLineData.clearValues();
            mLineChart.invalidate();
        }
    }

    @Override
    public boolean onBackPressed() {
        return EquipmentActivity.isStart;
    }
}
