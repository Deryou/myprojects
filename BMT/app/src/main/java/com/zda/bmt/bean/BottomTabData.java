package com.zda.bmt.bean;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zda.bmt.R;
import com.zda.bmt.gloable.AppConstant;
import com.zda.bmt.ui.fragment.EquipmentFragment;
import com.zda.bmt.ui.fragment.HumidityFragment;
import com.zda.bmt.ui.fragment.OxygenFragment;
import com.zda.bmt.ui.fragment.TemperatureFragment;

/**
 * Created by MR on 2017/4/28.
 */

public class BottomTabData {
    public static final int[] mTabRes = new int[]{R.mipmap.icon_tab_form_normal,R.mipmap.icon_tab_temp_normal,R.mipmap.icon_tab_humdity_normal,R.mipmap.icon_tab_oxygen_normal};
    public static final int[] mTabResPressed = new int[]{R.mipmap.icon_tab_form_select,R.mipmap.icon_tab_temp_select,R.mipmap.icon_tab_humdity_select,R.mipmap.icon_tab_oxygen_select};
    public static final String[] mTableTitle = new String[]{"设备", "温度", "湿度", "氧浓度"};

    public static Fragment[] getFragments() {
        Fragment fragment[] = new Fragment[4];
        fragment[AppConstant.EQUIPMENTFRAGMENT] = EquipmentFragment.newInstance();
        fragment[AppConstant.TEMPERATUREFRAGMENT] = TemperatureFragment.newInstance();
        fragment[AppConstant.HUMIDITYFRAGMENT] = HumidityFragment.newInstance();
        fragment[AppConstant.OXYGENFRAGMENT] = OxygenFragment.newInstance();
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
