package com.zda.bmt.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zda.bmt.R;
import com.zda.bmt.callback.EquimentCallback;
import com.zda.bmt.util.Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends Fragment implements EquimentCallback {

    @BindView(R.id.form_company)
    EditText mFormCompany;
    @BindView(R.id.form_equip_name)
    EditText mFormEquipName;
    @BindView(R.id.form_manufacturer)
    EditText mFormManufacturer;
    @BindView(R.id.form_factory_serial_num)
    EditText mFormFactorySerialNum;
    @BindView(R.id.form_equip_type)
    EditText mFormEquipType;
    @BindView(R.id.form_equip_number)
    EditText mFormEquipNumber;
    private Unbinder mUnbinder;

    public static EquipmentFragment newInstance() {
        EquipmentFragment fragment = new EquipmentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Map<String, String> getEquipData() {
        Map<String, String> map = new HashMap<>();
        map.put("$WTDW$", mFormCompany.getText()==null?"":mFormCompany.getText().toString());
        map.put("$QJMC$", mFormEquipName.getText()==null?"":mFormEquipName.getText().toString());
        map.put("$SCCS$", mFormManufacturer.getText()==null?"":mFormManufacturer.getText().toString());
        map.put("$CCBH$", mFormFactorySerialNum.getText()==null?"":mFormFactorySerialNum.getText().toString());
        map.put("$XHGG$", mFormEquipType.getText()==null?"":mFormEquipType.getText().toString());
        map.put("$SBBH$", mFormEquipNumber.getText()==null?"":mFormEquipNumber.getText().toString());
        map.put("$JCRQ$", Util.getNowTime());
        return map;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
