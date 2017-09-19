package com.mr.mrdetect.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.module.bean.ControlerBean;

import java.util.List;

/**
 * Created by MR on 2017/9/6.
 */

public class ControlerAdapter extends BaseQuickAdapter<ControlerBean.ControlorData,BaseViewHolder>{

    public ControlerAdapter(@Nullable List<ControlerBean.ControlorData> data) {
        super(R.layout.item_list_controlor, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ControlerBean.ControlorData item) {
        helper.setText(R.id.controler_name, item.name);
        helper.setText(R.id.controler_series, item.series);
        helper.setText(R.id.controler_identify, item.identification);
        helper.setText(R.id.controler_desc, item.desc);
    }
}
