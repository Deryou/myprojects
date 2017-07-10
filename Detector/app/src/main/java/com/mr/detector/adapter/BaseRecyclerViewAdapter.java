package com.mr.detector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.detector.adapter.holder.BaseHolder;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by MR on 2017/5/15.
 * 抽取RecyclerView基类
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    protected Context mContext;
    protected List<T> mList;
    protected T mDataItem;
    //自定义点击事件声明
    private OnItemClickListener mOnItemClickListener;
    private BaseHolder mHolder;

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getView(), parent, false);
        mHolder = getHolder(view);
        ButterKnife.bind(mHolder, view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        mDataItem = mList.get(position);
        holder.setData(mDataItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder,position);
            }
        });
        doExtra(mDataItem,holder);
    }

    /**
     * 做一些其他处理
     * @param dataItem
     */
    public void doExtra(T dataItem,BaseHolder holder) {

    }

    public List<T> getData() {
        return mList;
    }

    /**
     * 获取ViewHolder实例对象
     *
     * @return
     */
    protected abstract BaseHolder getHolder(View itemView);

    /**
     * 获取布局view
     *
     * @return
     */
    public abstract int getView();

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        } else {
            return 0;
        }
    }

    public interface OnItemClickListener {
        /**
         * 单击事件回调方法
         */
        void onItemClick(BaseHolder holder,int position);
    }
}
