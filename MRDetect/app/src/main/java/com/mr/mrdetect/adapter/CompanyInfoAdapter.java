package com.mr.mrdetect.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.module.bean.CompanyInfoBean;

import java.util.List;

/**
 * Created by MR on 2017/9/14.
 */

public class CompanyInfoAdapter extends BaseQuickAdapter<CompanyInfoBean.CompanyData,
        BaseViewHolder> {

    public CompanyInfoAdapter(@Nullable List<CompanyInfoBean.CompanyData> data) {
        super(R.layout.item_display_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyInfoBean.CompanyData item) {
        helper.setText(R.id.company_name, item.name);
        helper.setText(R.id.company_desc, item.desc);
    }
}
