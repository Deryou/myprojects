package com.zda.bmt.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.zda.bmt.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MR on 2017/7/20.
 */

public abstract class BaseFragment extends Fragment {
    public LineData mLineData;
    public Unbinder mUnbinder;
    @BindView(R.id.chart)
    LineChart mLineChart;
    Message msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(getResId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    protected void initView() {
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

//        mLineData = new LineData();
//        mLineChart.setData(mLineData);
//        XAxis xl = mLineChart.getXAxis();
//        xl.setDrawGridLines(false);
//        xl.setAvoidFirstLastClipping(true);
//        xl.setEnabled(true);
    }

    /**
     * 设置图表数据
     *
     * @param dataValue 接收到的数据，添加到图中
     */
    public void setChartData(List<Float> dataValue) {
        for (int i = 0; i < dataValue.size() - 1; i++) {
            setChartData(dataValue.get(i), i);
        }
    }

    public void setChartData(float dataValue) {
        setChartData(dataValue, 0);
    }

    public void setChartData(float dataValue, int type) {
        if (mLineChart != null) {
            mLineData = mLineChart.getData();
            if (mLineData != null) {
                ILineDataSet set = mLineData.getDataSetByIndex(type);
                if (set == null) {
                    set = createSet(type);
                    mLineData.addDataSet(set);
                }
                mLineData.addEntry(new Entry(set.getEntryCount(), dataValue), type);
                mLineData.notifyDataChanged();
                mLineChart.notifyDataSetChanged();
                mLineChart.setVisibleXRangeMaximum(30);
                mLineChart.moveViewToX(mLineData.getEntryCount());
            } else {
                mLineData = new LineData();
                mLineChart.setData(mLineData);
                XAxis xl = mLineChart.getXAxis();
                xl.setDrawGridLines(false);
                xl.setAvoidFirstLastClipping(true);
                xl.setEnabled(true);
            }
        }
    }

    /**
     * 创建线条
     *
     * @return
     */
    public ILineDataSet createSet(int type) {
        LineDataSet set = new LineDataSet(null, setDataName(type));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        switch (type) {
            case 0:
                set.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                break;
            case 1:
                set.setColor(ColorTemplate.COLORFUL_COLORS[1]);
                break;
            case 2:
                set.setColor(ColorTemplate.COLORFUL_COLORS[2]);
                break;
            case 3:
                set.setColor(ColorTemplate.COLORFUL_COLORS[3]);
                break;
            case 4:
                set.setColor(ColorTemplate.COLORFUL_COLORS[4]);
                break;
            default:
                break;
        }
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighlightEnabled(false);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    /**
     * 设置图表的名称
     *
     * @return
     */
    public String setDataName(int type) {
        return null;
    }

    public abstract int getResId();

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        if (mLineChart != null) {
            mLineChart.clear();
            mLineChart = null;
        }
        super.onDestroy();
    }
}
