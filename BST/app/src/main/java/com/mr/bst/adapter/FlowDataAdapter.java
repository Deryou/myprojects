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

public class FlowDataAdapter extends BaseItemDraggableAdapter<List<float[]>, BaseViewHolder> {
    public FlowDataAdapter(@Nullable List<List<float[]>> data) {
        super(R.layout.item_data_flow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, List<float[]> item) {
        String ave = "";
        for (int i = 0; i < item.size(); i++) {
            float data[] = item.get(i);
            switch (i) {
                case 0:
                    helper.setText(R.id.r1_c1, data[0] + "");
                    helper.setText(R.id.r2_c1, data[1] + "");
                    helper.setText(R.id.r3_c1, data[2] + "");
                    helper.setText(R.id.ave_c1, data[3] + "");
                    break;
                case 1:
                    helper.setText(R.id.r1_c2, data[0] + "");
                    helper.setText(R.id.r2_c2, data[1] + "");
                    helper.setText(R.id.r3_c2, data[2] + "");
                    helper.setText(R.id.ave_c2, data[3] + "");
                    break;
                case 2:
                    helper.setText(R.id.r1_c3, data[0] + "");
                    helper.setText(R.id.r2_c3, data[1] + "");
                    helper.setText(R.id.r3_c3, data[2] + "");
                    helper.setText(R.id.ave_c3, data[3] + "");
                    break;
            }
        }
    }
}
