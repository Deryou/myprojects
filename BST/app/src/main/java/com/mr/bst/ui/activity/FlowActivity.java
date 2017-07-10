package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mr.bst.R;
import com.mr.bst.adapter.FlowDataAdapter;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.util.TcpClient;
import com.mr.bst.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FlowActivity extends BaseChartActivity {
    private static final String TAG = "FlowActivity";

    @BindView(R.id.flow_a)
    TextView mFlowA;
    @BindView(R.id.flow_b)
    TextView mFlowB;
    @BindView(R.id.flow_c)
    TextView mFlowC;
    @BindView(R.id.line_chart)
    LineChart mLineChart;
    @BindView(R.id.get_distance)
    LinearLayout mGetDistance;
    @BindView(R.id.ensure_distance)
    Button mEnsureDistance;
    @BindView(R.id.x_distance)
    TextView mXDistance;
    @BindView(R.id.y_distance)
    TextView mYDistance;
    @BindView(R.id.add_text_pot)
    Button mAddTextPot;
    @BindView(R.id.flow_in_recyclerview)
    RecyclerView mFlowInRecyclerview;
    @BindView(R.id.flow_out_recyclerview)
    RecyclerView mFlowOutRecyclerview;
    @BindView(R.id.custom_title)
    TextView mCustomTitle;
    @BindView(R.id.flow_switch)
    Switch mFlowSwitch;
    float sum = 0, ave = 0;

    private List<List<float[]>> upFlowList = new ArrayList<>();
    private List<List<float[]>> downFlowList = new ArrayList<>();
    private float[] aFlowData = new float[4];
    private float[] bFlowData = new float[4];
    private float[] cFlowData = new float[4];
    private List<float[]> mFlowDown;
    private List<float[]> mFlowUp;
    private float[] mFlowData = new float[4];
    private float[] mDistance = new float[2];
    private boolean isUpFlow = true;
    private int getPointNum = 7;
    private Map<String,String> dataMap;

    private FlowDataAdapter mFlowUpAdapter, mFlowDownAdapter;
    private ItemTouchHelper mFlowUpHelper, mFlowDownHelper;
    private ItemDragAndSwipeCallback mFlowUpDragCallback, mFlowDownDragCallback;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TastyToast.makeText(getApplicationContext(), "风速数据请求成功", TastyToast
                            .LENGTH_LONG, TastyToast.SUCCESS);
                    break;
                case 1:
                    setChartData(mFlowData);
                    if (isEnsure && count < 3) {
                        mAddTextPot.setEnabled(false);
                        for (int i = 0; i < mFlowData.length; i++) {
                            switch (i) {
                                case 0:
                                    aFlowData[count] = mFlowData[i];
                                    break;
                                case 1:
                                    bFlowData[count] = mFlowData[i];
                                    break;
                                case 2:
                                    cFlowData[count] = mFlowData[i];
                                    break;
                            }
                        }
                        count++;
                        if (count > 2) {
                            aFlowData[3] = Util.getAveDelOne(aFlowData);
                            bFlowData[3] = Util.getAveDelOne(bFlowData);
                            isEnsure = false;
                            mAddTextPot.setEnabled(true);
                            if (isUpFlow) {
                                if (upFlowList.size() < getPointNum) {
                                    mFlowUp = new ArrayList<>();
                                    mFlowUp.add(aFlowData);
                                    mFlowUp.add(bFlowData);
//                                    mFlowUp.add(cFlowData);
                                    upFlowList.add(mFlowUp);
                                    mFlowUpAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (downFlowList.size() < getPointNum) {
                                    mFlowDown = new ArrayList<>();
                                    cFlowData[3] = Util.getAveDelOne(cFlowData);
                                    mFlowDown.add(aFlowData);
                                    mFlowDown.add(bFlowData);
                                    mFlowDown.add(cFlowData);
                                    downFlowList.add(mFlowDown);
                                    mFlowDownAdapter.notifyDataSetChanged();
                                }
                            }
                            synchronized (obj) {
                                isDistance = true;
                            }
                            sendData();
                        }
                    }
                    mFlowA.setText(mFlowData[0] + "");
                    mFlowB.setText(mFlowData[1] + "");
                    mFlowC.setText(mFlowData[2] + "");
                    break;
                case 2:
                    TastyToast.makeText(getApplicationContext(), "测距数据请求成功", TastyToast
                            .LENGTH_SHORT, TastyToast.SUCCESS);
                    break;
                case 3:
                    mXDistance.setText(mDistance[0] + "");
                    mYDistance.setText(mDistance[1] + "");
                    break;
                case 4:
                    TastyToast.makeText(getApplicationContext(), "测距失败", TastyToast
                            .LENGTH_SHORT, TastyToast.ERROR);
                    break;
                default:
                    break;
            }
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FlowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mFlowUpAdapter = new FlowDataAdapter(upFlowList);
        mFlowDownAdapter = new FlowDataAdapter(downFlowList);
        mFlowUpDragCallback = new ItemDragAndSwipeCallback(mFlowUpAdapter);
        mFlowDownDragCallback = new ItemDragAndSwipeCallback(mFlowDownAdapter);
        mFlowUpHelper = new ItemTouchHelper(mFlowUpDragCallback);
        mFlowDownHelper = new ItemTouchHelper(mFlowDownDragCallback);
        mFlowUpHelper.attachToRecyclerView(mFlowInRecyclerview);
        mFlowDownHelper.attachToRecyclerView(mFlowOutRecyclerview);

        mFlowUpDragCallback.setSwipeMoveFlags(ItemTouchHelper.START);
        mFlowDownDragCallback.setSwipeMoveFlags(ItemTouchHelper.START);
        mFlowUpAdapter.enableSwipeItem();
        mFlowDownAdapter.enableSwipeItem();
        mFlowInRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mFlowOutRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mFlowInRecyclerview.setAdapter(mFlowUpAdapter);
        mFlowOutRecyclerview.setAdapter(mFlowDownAdapter);

        //状态切换开关事件监听
        mFlowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isUpFlow = false;
                } else {
                    isUpFlow = true;
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_flow;
    }

    @Override
    public String setToolbarTitle() {
        return "流速测试";
    }

    @Override
    public String setDataName(int type) {
        switch (type) {
            case AppConstant.FLOW_A:
                return "FA";
            case AppConstant.FLOW_B:
                return "FB";
            case AppConstant.FLOW_C:
                return "FC";
        }
        return null;
    }

    @Override
    protected void sendRequestData(String equipNum) {
        if (isDistance) {
            mTcpServer.write(Util.getDistanceRequestData(equipNum));
        } else {
            mTcpServer.write(Util.getFlowRequestData(equipNum));
        }
        mTcpServer.openConn(true);
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        synchronized (obj) {
            msg = Message.obtain();
            String sendData = new String(data);

            if (sendData.contains("++")) {
                String subData = sendData.split("\\+\\+")[0];
                Log.e(TAG, "OnDataReceived: " + subData);
                if (subData.contains("OK")) {
                    if (subData.substring(subData.indexOf("end") + 3, subData.indexOf("OK")).equals
                            ("FS")) {
                        msg.what = 0;
                    }
                    if (subData.substring(subData.indexOf("end") + 3, subData.indexOf("OK")).trim()
                            .equals("LL")) {
                        msg.what = 2;
                    }
                } else if (subData.substring(subData.indexOf("LL") + 2).equals("error")) {
                    msg.what = 4;
                }
            } else if (sendData.trim().endsWith("end")) {
                if (!isDistance) {
                    mFlowData = Util.getFlowData(sendData);
                    for (int i = 0; i < 3; i++) {
                        sum += mFlowData[i];
                    }
                    ave = ((sum / 3f) * 1000) / 1000f;
                    mFlowData[4] = ave;
                    msg.what = 1;
                } else {
                    mDistance = Util.getDistanceData(sendData);
                    msg.what = 3;
                }

            } else {
                msg.what = 5;
            }
            mHandler.sendMessage(msg);
        }

    }

    public void clearData() {
        mLineChart.clearValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTcpServer.openConn(false);
    }

    @OnClick({R.id.ensure_distance, R.id.add_text_pot, R.id.save_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ensure_distance:
                mGetDistance.setVisibility(View.GONE);
                mAddTextPot.setVisibility(View.VISIBLE);
                synchronized (obj) {
                    isDistance = false;
                }
                sendData();
                break;
            case R.id.add_text_pot:
                mAddTextPot.setVisibility(View.GONE);
                mGetDistance.setVisibility(View.VISIBLE);
                count = 0;
                isEnsure = true;
                break;
            case R.id.save_data:
                dataMap = new HashMap<>();
                for (int i = 1; i <= 7; i++) {
                    if (mFlowUp.get(i - 1) != null) {
                        dataMap.put("$IA_1_" + i + "$", "");
                        dataMap.put("$IA_2_" + i + "$", "");
                        dataMap.put("$IA_ave_" + i + "$", "");
                    }
                }
                Gson gson = new Gson();
                String testData = gson.toJson(upFlowList);
                Log.e(TAG, "onViewClicked: 测试保存的数据：" + testData);
                Type type = new TypeToken<List<List<float[]>>>() {
                }.getType();
                Gson gs = new Gson();
                List<List<float[]>> asd = gs.fromJson(testData, type);
                for (List<float[]> a : asd) {
                    for (int i = 0; i < a.size(); i++) {
                        Log.e(TAG, "onViewClicked: 单个数组数据："+a.get(i)[0]+"   "+a.get(i)[1]+"   "+a
                                .get(i)[2]+"   "+a.get(i)[3]);
                    }
                }
                TastyToast.makeText(getApplicationContext(), "数据保存成功", TastyToast.LENGTH_SHORT,
                        TastyToast.INFO);
                break;
        }
    }
}
