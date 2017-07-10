package com.mr.detector.adapter.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mr.detector.R;
import com.mr.detector.bean.WifiItem;
import com.mr.detector.gloable.AppConstants;

import butterknife.BindView;

/**
 * Created by MR on 2017/5/15.
 */

public class WifiViewHolder extends BaseHolder<WifiItem> {
    @BindView(R.id.wifi_name)
    TextView mWifiName;
    @BindView(R.id.wifi_state)
    ImageView mWifiState;
    @BindView(R.id.conn_state)
    TextView mConnState;

    public WifiViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(WifiItem item) {
        int level = item.getScanResult().level;
        if (level <= 0 && level >= -50) {
            mWifiState.setImageResource(R.mipmap.wifi_single_level_1);
        } else if (level < -50 && level >= -70) {
            mWifiState.setImageResource(R.mipmap.wifi_single_level_2);
        } else if (level < -70 && level >= -80) {
            mWifiState.setImageResource(R.mipmap.wifi_single_level_3);
        } else if (level < -80 && level >= -100) {
            mWifiState.setImageResource(R.mipmap.wifi_single_level_4);
        }
        mWifiName.setText(item.getScanResult().SSID == null ? "" : item.getScanResult().SSID);
    }

    @Override
    public void refreshView(int connCode) {

    }

    @Override
    public void isRefresh(boolean isRefresh) {
        if (isRefresh) {
            mConnState.setVisibility(View.VISIBLE);
            mConnState.setText("已连接");
            mConnState.setTextColor(Color.parseColor("#326EB4"));
        } else {
            mConnState.setVisibility(View.INVISIBLE);
        }
    }
}
