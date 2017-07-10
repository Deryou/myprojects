package com.mr.bst.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mr.bst.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MR on 2017/6/20.
 */

public abstract class BaseFragment extends Fragment {
    @BindView(R.id.line_chart)
    LineChart mLineChart;
    private LineData mLineData;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(getResourceId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Description description = new Description();
        description.setText("明瑞检测");
        mLineChart.setDescription(description);
        mLineChart.setNoDataText("暂时尚无数据");
        mLineChart.setTouchEnabled(true);
        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setPinchZoom(true);
        //设置图表的背景色为灰色
        mLineChart.setBackgroundColor(Color.LTGRAY);

    }

    public abstract int getResourceId();

    public void setChartData(float dataValue) {
        mLineData = mLineChart.getData();
        if (mLineData != null) {
            ILineDataSet set = mLineData.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                mLineData.addDataSet(set);
            }
            mLineData.addEntry(new Entry(set.getEntryCount(), dataValue), 0);
            mLineData.notifyDataChanged();
            mLineChart.notifyDataSetChanged();
            mLineChart.setVisibleXRangeMaximum(30);
            mLineChart.moveViewToX(mLineData.getEntryCount());
        } else {
            LineData lineData = new LineData();
            mLineChart.setData(lineData);
            XAxis xl = mLineChart.getXAxis();
            xl.setDrawGridLines(false);
            xl.setAvoidFirstLastClipping(true);
            xl.setEnabled(true);
        }
    }

    public ILineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, setDataName());
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
//        set.setCircleColor(Color.RED);
//        set.setCircleRadius(3f);
        //去掉圆点
        set.setDrawCircles(false);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.GRAY);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    public String setDataName() {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mLineChart != null) {
            mLineChart.clear();
            mLineChart = null;
        }
        mUnbinder.unbind();
        super.onDestroy();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param text
     * @return
     */
    protected boolean isNotEmptyString(String text) {
        return !TextUtils.isEmpty(text) && !text.equals("null");
    }
}
