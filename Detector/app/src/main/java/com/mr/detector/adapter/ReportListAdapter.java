package com.mr.detector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mr.detector.R;
import com.mr.detector.bean.ReportFile;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MR on 2017/5/9.
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {
    private List<ReportFile> mList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private ReportFile mReportFile;
    private HashMap<Integer, Boolean> selectedItem;
    private boolean selectMode = false;
    private boolean isClickable = true;

    public ReportListAdapter(Context context, List<ReportFile> list) {
        this.mContext = context;
        this.mList = list;
        selectedItem = new HashMap<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report_list, parent, false);
        ReportViewHolder viewHolder = new ReportViewHolder(view);
        ButterKnife.bind(viewHolder, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReportViewHolder holder, final int position) {
        mReportFile = mList.get(position);
        holder.mReportName.setText(mReportFile.getFileName());
        holder.mFileSize.setText(mReportFile.getFileSize());
        holder.mFileTime.setText(mReportFile.getFileTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(mReportFile.getFilePath());
            }
        });
        holder.mFileChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedItem.put(position, isChecked);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(position);
                return true;
            }
        });
        if (selectMode) {
            holder.mFileChecked.setVisibility(View.VISIBLE);
            holder.mFileChecked.setChecked(selectedItem.get(position));
        } else {
            holder.mFileChecked.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 全选/取消全选
     * @param selectMode
     */
    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
        selectedItem.clear();
        for (int i = 0; i < getItemCount(); i++) {
            selectedItem.put(i, false);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 获取选取模式，是否全选
     * @return
     */
    public boolean getSelectMode() {
        return selectMode;
    }

    public void setSelected(int position) {
        if (selectedItem.get(position)) {
            selectedItem.put(position, false);
        } else {
            selectedItem.put(position, true);
        }
        this.notifyDataSetChanged();
    }

    public boolean getSelected(int position) {
        if (selectedItem.size() > position) {
            return selectedItem.get(position);
        } else {
            return false;
        }
    }

    public interface OnItemClickListener {
        /**
         * 单击事件响应监听
         */
        void onItemClick(String filePath);

        /**
         * 长按事件响应监听
         * @param position 位置
         */
        void onItemLongClick(int position);
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.file_name)
        TextView mReportName;
        @BindView(R.id.file_size)
        TextView mFileSize;
        @BindView(R.id.file_time)
        TextView mFileTime;
        @BindView(R.id.file_item_checked)
        CheckBox mFileChecked;

        public ReportViewHolder(View itemView) {
            super(itemView);
        }
    }
}
