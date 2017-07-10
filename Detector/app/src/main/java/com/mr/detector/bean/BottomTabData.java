package com.mr.detector.bean;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mr.detector.R;
import com.mr.detector.gloable.AppConstants;
import com.mr.detector.ui.fragment.EquipmentFragment;
import com.mr.detector.ui.fragment.HumidityFragment;
import com.mr.detector.ui.fragment.OxygenFragment;
import com.mr.detector.ui.fragment.TemperatureFragment;

/**
 * Created by MR on 2017/4/28.
 */

public class BottomTabData {
    public static final int[] mTabRes = new int[]{R.mipmap.icon_tab_form_normal,R.mipmap.icon_tab_temp_normal,R.mipmap.icon_tab_humdity_normal,R.mipmap.icon_tab_oxygen_normal};
    public static final int[] mTabResPressed = new int[]{R.mipmap.icon_tab_form_select,R.mipmap.icon_tab_temp_select,R.mipmap.icon_tab_humdity_select,R.mipmap.icon_tab_oxygen_select};
    public static final String[] mTableTitle = new String[]{"设备", "温度", "湿度", "氧浓度"};

    public static Fragment[] getFragments() {
        Fragment fragment[] = new Fragment[4];
        fragment[AppConstants.EQUIPMENTFRAGMENT] = EquipmentFragment.newInstance();
        fragment[AppConstants.TEMPERATUREFRAGMENT] = TemperatureFragment.newInstance();
        fragment[AppConstants.HUMIDITYFRAGMENT] = HumidityFragment.newInstance();
        fragment[AppConstants.OXYGENFRAGMENT] = OxygenFragment.newInstance();
        return fragment;
    }

    public static View getTabView(Context context, int postion) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_tab, null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabIcon.setImageResource(mTabRes[postion]);
        tabText.setText(mTableTitle[postion]);
        return view;
    }
}
