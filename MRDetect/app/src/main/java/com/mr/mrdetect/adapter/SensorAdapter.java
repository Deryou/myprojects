package com.mr.mrdetect.adapter;

import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.module.bean.ControlerBean;
import com.mr.mrdetect.mvp.module.bean.SensorBean;

import java.util.List;

/**
 * Created by MR on 2017/9/6.
 */

public class SensorAdapter extends BaseQuickAdapter<SensorBean.SensorData,BaseViewHolder>{

    public SensorAdapter(@Nullable List<SensorBean.SensorData> data) {
        super(R.layout.item_list_sensor, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SensorBean.SensorData item) {
        helper.setText(R.id.sensor_name, item.name);
        helper.setText(R.id.sensor_identify, item.identification);
        helper.setText(R.id.sensor_device_identify, item.deviceIdentify);
        helper.setText(R.id.sensor_temp, item.temp + "℃");
        helper.setText(R.id.sensor_desc, item.desc);
        Log.e(TAG, "convert: 时间为："+SystemClock.uptimeMillis() );
    }
}
