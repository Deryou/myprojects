package com.mr.bst.ui.activity;

import android.graphics.Color;
import android.os.Message;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mr.bst.R;
import com.mr.bst.callback.ServerCallback;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.util.TcpServer;
import com.mr.bst.util.Util;
import com.mr.bst.widget.MEditDialog;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by MR on 2017/6/22.
 * 用于activity进行图表数据操作的基类
 */

public abstract class BaseChartActivity extends BaseActivity implements ServerCallback {
    public TcpServer mTcpServer;
    @BindView(R.id.line_chart)
    LineChart mLineChart;
    Message msg;
    private LineData mLineData;
    private Thread writeThread;
    private MEditDialog mMEditDialog;
    public Object obj = new Object();
    public Object lock = new Object();
    public int count = 0;
    public boolean isEnsure = false;
    public PromptDialog mDialog;

    @Override
    public void initView() {
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

        //设置数据监听
        mTcpServer = TcpServer.getInstance(AppConstant.HOTSPOT_PORT);
        mTcpServer.setServerCallback(this);

        sendData();
    }

    /**
     * 设置图表数据
     *
     * @param dataValue 接收到的数据，添加到图中
     * @param type      数据类型，以便用于分类折线，
     */
//    public void setChartData(float dataValue, int type) {
//        mLineData = mLineChart.getData();
//        if (mLineData != null) {
//            ILineDataSet set = mLineData.getDataSetByIndex(type);
//            if (set == null) {
//                set = createSet(type);
//                mLineData.addDataSet(set);
//            }
//            mLineData.addEntry(new Entry(set.getEntryCount(), dataValue), type);
//            mLineData.notifyDataChanged();
//            mLineChart.notifyDataSetChanged();
//            mLineChart.setVisibleXRangeMaximum(30);
//            mLineChart.moveViewToX(mLineData.getEntryCount());
//        } else {
//            mLineData = new LineData();
//            mLineChart.setData(mLineData);
//            XAxis xl = mLineChart.getXAxis();
//            xl.setDrawGridLines(false);
//            xl.setAvoidFirstLastClipping(true);
//            xl.setEnabled(true);
//        }
//    }

    /**
     * 设置图表数据
     *
     * @param dataValue 接收到的数据，添加到图中
     */
    public void setChartData(float[] dataValue) {
        mLineData = mLineChart.getData();
        if (mLineData != null) {
            for (int i = 0; i < dataValue.length - 1; i++) {
                ILineDataSet set = mLineData.getDataSetByIndex(i);
                if (set == null) {
                    set = createSet(i);
                    mLineData.addDataSet(set);
                }
                mLineData.addEntry(new Entry(set.getEntryCount(), dataValue[i]), i);
                mLineData.notifyDataChanged();
                mLineChart.notifyDataSetChanged();
                mLineChart.setVisibleXRangeMaximum(30);
                mLineChart.moveViewToX(mLineData.getEntryCount());
            }
        } else {
            mLineData = new LineData();
            mLineChart.setData(mLineData);
            XAxis xl = mLineChart.getXAxis();
            xl.setDrawGridLines(false);
            xl.setAvoidFirstLastClipping(true);
            xl.setEnabled(true);
        }
    }

    /**
     * 设置图表的名称
     *
     * @return
     */
    public String setDataName(int type) {
        return null;
    }

    /**
     * 创建线条
     *
     * @return
     */
    public ILineDataSet createSet(int type) {
        LineDataSet set = new LineDataSet(null, setDataName(type));
        set.setAxisDependency(AxisDependency.LEFT);
        switch (type) {
            case 0:
                set.setColor(ColorTemplate.LIBERTY_COLORS[0]);
                break;
            case 1:
                set.setColor(ColorTemplate.LIBERTY_COLORS[1]);
                break;
            case 2:
                set.setColor(ColorTemplate.LIBERTY_COLORS[2]);
                break;
            default:
                break;
        }
//        set.setColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
//        set.setCircleColor(Color.WHITE);
//        set.setCircleRadius(2f);
        set.setDrawCircles(false);
        set.setDrawCircles(false);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
//        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setHighlightEnabled(false);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    protected void onDestroy() {
        if (mLineChart != null) {
            mLineChart.clear();
            mLineChart = null;
        }
        super.onDestroy();
    }

    public void sendData() {
        final String tpNum = Util.getEquipNum();
        if (writeThread != null) {
            writeThread.interrupt();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (tpNum != null) {
                    sendRequestData(tpNum);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMEditDialog = new MEditDialog(BaseChartActivity.this);
                            mMEditDialog.setTitle("请输入当前设备的编号！")
                                    .setTitleRow2("设备编号：");
                            final String equipNum = mMEditDialog.getTitleRow2Msg();
                            mMEditDialog.setOnEnsureClickListener("YES", new MEditDialog
                                    .onEnsureClickListener() {

                                @Override
                                public void onEnsureClick() {
                                    if (equipNum != null) {
                                        Util.saveEquipNum(equipNum);
                                        sendRequestData(equipNum);
                                        mMEditDialog.dismiss();
                                    } else {
                                        TastyToast.makeText(getApplicationContext(),
                                                "输入为空，请再次输入！", TastyToast.LENGTH_LONG, TastyToast
                                                        .WARNING);
                                    }
                                }
                            });
                            mMEditDialog.setOnCancelClickListener("NO", new MEditDialog
                                    .onCancelClickListener() {

                                @Override
                                public void onCancelClick() {
                                    mMEditDialog.dismiss();
                                }
                            });
                            mMEditDialog.show();
                        }
                    });
                }
            }
        };
        writeThread = new Thread(runnable);
        writeThread.start();
    }

    protected abstract void sendRequestData(String equipNum);
}
