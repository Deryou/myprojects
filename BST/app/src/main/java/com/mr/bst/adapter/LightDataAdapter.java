package com.mr.bst.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mr.bst.R;

import java.util.List;

/**
 * Created by MR on 2017/6/29.
 * 用于照度、流速数据适配
 */

public class LightDataAdapter extends BaseItemDraggableAdapter<float[], BaseViewHolder> {
    public LightDataAdapter(@Nullable List<float[]> data) {
        super(R.layout.item_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, float[] item) {
        float sum = 0, ave;
        helper.setText(R.id.item_id, "第" + (helper.getLayoutPosition() + 1) + "点:");
        for (int i = 0; i < item.length; i ++) {
            switch (i) {
                case 0:
                    helper.setText(R.id.first_pot, item[i] + ",");
                    break;
                case 1:
                    helper.setText(R.id.second_pot, item[i] + ",");
                    break;
                case 2:
                    helper.setText(R.id.third_pot, item[i] + ",");
                    break;
                case 3:
                    helper.setText(R.id.ave_pot, item[i] + "");
                    break;
            }
        }

    }
}
