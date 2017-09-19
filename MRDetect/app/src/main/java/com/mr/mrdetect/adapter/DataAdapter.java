package com.mr.mrdetect.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.module.bean.DataBean;
import com.mr.mrdetect.utils.Util;

import java.util.List;

/**
 * Created by MR on 2017/9/8.
 */

public class DataAdapter extends BaseQuickAdapter<DataBean.Data, BaseViewHolder> {
    public DataAdapter(@Nullable List<DataBean.Data> data) {
        super(R.layout.item_data_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean.Data item) {
        helper.setText(R.id.device_identify, item.deviceIdentify);
        helper.setText(R.id.sensor_identify, item.sensorIdentify);
        helper.setText(R.id.data_temp, item.temp + "℃");
        helper.setText(R.id.upload_user, item.userName);
        helper.setText(R.id.update_time, Util.getDateTime(item.updateTime));
    }
}
