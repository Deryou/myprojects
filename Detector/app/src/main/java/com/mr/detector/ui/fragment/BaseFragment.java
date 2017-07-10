package com.mr.detector.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mr.detector.R;
import com.mr.detector.callback.DataResultCallback;
import com.mr.detector.widget.CheckMarkView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    public Context mContext;
    public Unbinder mUnbinder;
    public LineDataSet dataSet;
    public LineData mLineData;
    public ArrayList<ILineDataSet> sets = new ArrayList<>();
    public ArrayList<Entry> entries;
    @BindView(R.id.chart)
    LineChart mLineChart;
    public int dataCount = 1;
    public PromptDialog mPromptDialog;
    public DataResultCallback mResultCallback;

    public void setDataResultCallback(DataResultCallback callback) {
        this.mResultCallback = callback;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getResId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPromptDialog = new PromptDialog(getActivity());
        mPromptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        initView();
        return view;
    }

    public void initView() {
        Description description = new Description();
        description.setText("明瑞检测");
        mLineChart.setDescription(description);
        mLineChart.setNoDataText("暂时尚无数据");
        CheckMarkView markView = new CheckMarkView(getContext(), R.layout.check_marker_view);
        markView.setChartView(mLineChart);
        mLineChart.setMarker(markView);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundColor(Color.LTGRAY);

    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        if (mLineChart != null) {
            mLineChart.clear();
            mLineChart = null;
        }
        super.onDestroy();
    }

    public abstract int getResId();

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
