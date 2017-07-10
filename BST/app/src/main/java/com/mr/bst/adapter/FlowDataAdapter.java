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
        super(R.layout.item_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, List<float[]> item) {
        String ave = "";
        helper.setText(R.id.item_id, "第" + (helper.getLayoutPosition() + 1) + "点:");
        for (int i = 0; i < item.size(); i++) {
            float data[] = item.get(i);
            switch (i) {
                case 0:
                    helper.setText(R.id.first_pot, data[0]+ ","+data[1]+","+data[2]);
                    ave += data[3]+"/";
                    break;
                case 1:
                    helper.setText(R.id.second_pot,data[0]+ ","+data[1]+","+data[2]);
                    ave += data[3]+"/";
                    break;
                case 2:
                    helper.setText(R.id.third_pot, data[0]+ ","+data[1]+","+data[2]);
                    ave += data[3];
                    break;
            }
            helper.setText(R.id.ave_pot, ave);
        }
    }
}
