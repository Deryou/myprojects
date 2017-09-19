package com.mr.mrdetect.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.module.bean.ReportBean;

import java.util.List;

/**
 * Created by MR on 2017/9/7.
 */

public class ReportListAdapter extends BaseQuickAdapter<ReportBean.ReportData,BaseViewHolder>{
    public ReportListAdapter(@Nullable List<ReportBean.ReportData> data) {
        super(R.layout.item_report_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportBean.ReportData item) {
        helper.setText(R.id.file_name, item.title);
        helper.setText(R.id.file_time, item.updateTime);
        helper.setText(R.id.file_size, item.fileName);
    }
}
