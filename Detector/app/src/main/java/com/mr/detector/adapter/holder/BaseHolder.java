package com.mr.detector.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MR on 2017/5/15.
 * 基类holder
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{


    public BaseHolder(View itemView) {
        super(itemView);
    }

    /**
     * 设置数据以更新视图
     * @param data 数据
     */
    public abstract void setData(T data);

    /**
     * 用于视图状态改变时刷新
     */
    public void refreshView(int connCode) {

    }

    /**
     * 是否刷新
     * @param isRefresh
     */
    public void isRefresh(boolean isRefresh) {

    }
}
