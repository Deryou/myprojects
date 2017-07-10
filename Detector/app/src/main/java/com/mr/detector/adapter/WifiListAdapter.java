package com.mr.detector.adapter;

import android.content.Context;
import android.view.View;

import com.mr.detector.R;
import com.mr.detector.adapter.holder.BaseHolder;
import com.mr.detector.adapter.holder.WifiViewHolder;
import com.mr.detector.bean.WifiItem;

import java.util.List;

/**
 * Created by MR on 2017/5/11.
 * 获取可用wifi列表的适配器
 */

public class WifiListAdapter extends BaseRecyclerViewAdapter<WifiItem> {

    public WifiListAdapter(Context context, List<WifiItem> list) {
        super(context, list);
    }

    @Override
    protected BaseHolder getHolder(View itemView) {
        return new WifiViewHolder(itemView);
    }

    @Override
    public int getView() {
        return R.layout.item_wifi_conn;
    }

    @Override
    public void doExtra(WifiItem dataItem, BaseHolder holder) {
        holder.isRefresh(dataItem.isConn());
    }
}
