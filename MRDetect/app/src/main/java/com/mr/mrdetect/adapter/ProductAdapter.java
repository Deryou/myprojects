package com.mr.mrdetect.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.module.bean.ProductBean;

import java.util.List;

/**
 * Created by MR on 2017/9/6.
 */

public class ProductAdapter extends BaseQuickAdapter<ProductBean.ProductData,BaseViewHolder>{

    public ProductAdapter( @Nullable List<ProductBean.ProductData> data) {
        super(R.layout.item_list_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBean.ProductData item) {
        helper.setText(R.id.equip_name, item.name);
        helper.setText(R.id.equip_series, item.series);
        helper.setText(R.id.equip_display, item.description);
    }
}
