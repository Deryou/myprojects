package com.mr.bst.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.bst.R;
import com.mr.bst.bean.ReportFile;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MR on 2017/7/1.
 */

public class ReportListAdapter extends BaseQuickAdapter<ReportFile,ReportListAdapter.ReportViewHolder>{

    private HashMap<Integer,Boolean> selectedItem;
    private boolean selectMode = false;
    private boolean isClickable = true;

    public ReportListAdapter(@Nullable List<ReportFile> data) {
        super(R.layout.item_report_list, data);
        selectedItem = new HashMap<>();
    }

    @Override
    protected void convert(final ReportViewHolder helper, ReportFile item) {
        helper.mFileName.setText(item.getFileName());
        helper.mFileSize.setText(item.getFileSize());
        helper.mFileTime.setText(item.getFileTime());
        helper.mFileChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedItem.put(helper.getAdapterPosition(), isChecked);
            }
        });
        if (selectMode) {
            helper.mFileChecked.setVisibility(View.VISIBLE);
            helper.mFileChecked.setChecked(selectedItem.get(helper.getAdapterPosition()));
        } else {
            helper.mFileChecked.setVisibility(View.GONE);
        }
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

    class ReportViewHolder extends BaseViewHolder{
        @BindView(R.id.file_name)
        TextView mFileName;
        @BindView(R.id.file_size)
        TextView mFileSize;
        @BindView(R.id.file_time)
        TextView mFileTime;
        @BindView(R.id.file_item_checked)
        CheckBox mFileChecked;

        public ReportViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
