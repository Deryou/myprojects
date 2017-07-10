package com.mr.detector.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mr.detector.R;
import com.mr.detector.bean.ReportData;
import com.mr.detector.callback.EquipmentCallback;
import com.mr.detector.callback.ReportDataCallback;
import com.mr.detector.ui.activity.EquipmentActivity;
import com.mr.detector.util.Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;

/**
 * 设备表单填写界面
 */
public class EquipmentFragment extends Fragment implements EquipmentCallback {
    private static final String TAG = "EquipmentFragment";

    @BindView(R.id.form_company)
    EditText mCompany;
//    private EditText mCompany;
    @BindView(R.id.form_equip_name)
    EditText mEquipName;
    @BindView(R.id.form_manufacturer)
    EditText mManufaturer;
    @BindView(R.id.form_factory_serial_num)
    EditText mSerialNum;
    @BindView(R.id.form_equip_type)
    EditText mEquipType;
    @BindView(R.id.form_equip_number)
    EditText mEquipNum;

    private static Map<String, String> map;
    private Unbinder unbinder;


    public static EquipmentFragment newInstance() {
        EquipmentFragment fragment = new EquipmentFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
//        mCompany = (EditText) view.findViewById(R.id.form_company);
//        mEquipName = (EditText) view.findViewById(R.id.form_equip_name);
//        mManufaturer = (EditText) view.findViewById(R.id.form_manufacturer);
//        mSerialNum = (EditText) view.findViewById(R.id.form_factory_serial_num);
//        mEquipType = (EditText) view.findViewById(R.id.form_equip_type);
//        mEquipNum = (EditText) view.findViewById(R.id.form_equip_number);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void createForm(String fileName, String dataTime) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            saveEquipmentInfo();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void saveEquipmentInfo() {
        map = new HashMap<>();
        map.put("WTDW", mCompany.getText().toString()==null?"(空)":mCompany.getText().toString());
        map.put("QJMC", mEquipName.getText().toString()==null?"(空)":mEquipName.getText().toString());
        map.put("SCCS", mManufaturer.getText().toString()==null?"(空)":mManufaturer.getText().toString());
        map.put("CCBH", mSerialNum.getText().toString()==null?"(空)":mSerialNum.getText().toString());
        map.put("XHGG", mEquipType.getText().toString()==null?"(空)":mEquipType.getText().toString());
        map.put("SBBH", mEquipNum.getText().toString()==null?"(空)":mEquipNum.getText().toString());
        map.put("JCRQ", Util.getNowTime());
        EquipmentActivity.mReportData.setEquipmentInfo(map);
    }

    @Override
    public Map<String,String> getEqumentReport() {
        map = new HashMap<>();
        map.put("WTDW", mCompany.getText().toString()==null?"(空)":mCompany.getText().toString());
        map.put("QJMC", mEquipName.getText().toString()==null?"(空)":mEquipName.getText().toString());
        map.put("SCCS", mManufaturer.getText().toString()==null?"(空)":mManufaturer.getText().toString());
        map.put("CCBH", mSerialNum.getText().toString()==null?"(空)":mSerialNum.getText().toString());
        map.put("XHGG", mEquipType.getText().toString()==null?"(空)":mEquipType.getText().toString());
        map.put("SBBH", mEquipNum.getText().toString()==null?"(空)":mEquipNum.getText().toString());
        map.put("JCRQ", Util.getNowTime());
       return map;
    }
}
