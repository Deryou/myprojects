package com.zda.bmt.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zda.bmt.R;
import com.zda.bmt.bean.WifiItem;

import java.util.List;

/**
 * Created by MR on 2017/6/13.
 */

public class WifiListAdapter extends BaseQuickAdapter<WifiItem,BaseViewHolder>{
    public WifiListAdapter(@LayoutRes int layoutResId, @Nullable List<WifiItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiItem item) {
//        helper.addOnClickListener(R.id.wifi_conn_content);
        int level = item.getScanResult().level;
        if (level <= 0 && level >= -50) {
            helper.setImageResource(R.id.wifi_state, R.mipmap.wifi_single_level_1);
        } else if (level < -50 && level >= -70) {
            helper.setImageResource(R.id.wifi_state,R.mipmap.wifi_single_level_2);
        } else if (level < -70 && level >= -80) {
            helper.setImageResource(R.id.wifi_state,R.mipmap.wifi_single_level_3);
        } else if (level < -80 && level >= -100) {
            helper.setImageResource(R.id.wifi_state,R.mipmap.wifi_single_level_4);
        }
        helper.setText(R.id.wifi_name, item.getScanResult().SSID == null ? "" : item
                .getScanResult().SSID);
        if (item.isConn()) {
            helper.getView(R.id.conn_state).setVisibility(View.VISIBLE);
            helper.setText(R.id.conn_state, "已连接");
            helper.setTextColor(R.id.conn_state, Color.parseColor("#326EB4"));
        } else {
            helper.getView(R.id.conn_state).setVisibility(View.INVISIBLE);
        }
    }
}
