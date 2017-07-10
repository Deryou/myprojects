package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.google.gson.Gson;
import com.mr.bst.R;
import com.mr.bst.adapter.LightDataAdapter;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.util.TcpClient;
import com.mr.bst.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class LightActivity extends BaseChartActivity {
    private static final String TAG = "LightActivity";

    @BindView(R.id.light_a)
    TextView mLightA;
    @BindView(R.id.light_b)
    TextView mLightB;
    @BindView(R.id.light_switch)
    Switch mLightSwitch;
    @BindView(R.id.open_light_ave)
    EditText mOpenLightAve;
    @BindView(R.id.close_light_ave)
    EditText mCloseLightAve;
    @BindView(R.id.ensure_distance)
    Button mEnsureDistance;
    @BindView(R.id.x_distance)
    TextView mXDistance;
    @BindView(R.id.y_distance)
    TextView mYDistance;
    @BindView(R.id.get_distance)
    LinearLayout mGetDistance;
    @BindView(R.id.add_text_pot)
    Button mAddTextPot;
    @BindView(R.id.light_open_recyclerview)
    RecyclerView mLightOpenRecyclerview;
    @BindView(R.id.light_close_recyclerview)
    RecyclerView mLightCloseRecyclerview;
    @BindView(R.id.save_data)
    Button mSaveData;

    private float[] mLightData = new float[3];
    private float[] mDistance = new float[2];
    private float[] aLight = new float[4];
    private float[] bLight = new float[4];
    private List<float[]> mLightOpenList = new ArrayList<>();
    private List<float[]> mLightCloseList = new ArrayList<>();
    private float sum = 0, ave = 0;
    private boolean isLamp = true;
    private int isSixPoint = 6;

    private Map<String, String> dataMap;

    private LightDataAdapter mLightOnAdapter, mLightOffAdapter;
    private ItemTouchHelper mLightOnHelper, mLightOffHelper;
    private ItemDragAndSwipeCallback mLightOnDragCallback, mLightOffDragCallback;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    TastyToast.makeText(getApplicationContext(), "照度数据请求成功", TastyToast
                            .LENGTH_SHORT, TastyToast.SUCCESS);
                    break;
                case 1:
                    setChartData(mLightData);
                    if (isEnsure && count < 3) {
                        mAddTextPot.setEnabled(false);
                        for (int i = 0; i < mLightData.length; i++) {
                            switch (i) {
                                case 0:
                                    aLight[count] = mLightData[i];
                                    break;
                                case 1:
                                    bLight[count] = mLightData[i];
                                    break;
                            }
                        }
                        count++;
                        if (count > 2) {
                            aLight[3] = Util.getAveDelOne(aLight);
                            bLight[3] = Util.getAveDelOne(bLight);
                            isEnsure = false;
                            mAddTextPot.setEnabled(true);
                            if (isLamp) {
                                if (mLightOpenList.size() < isSixPoint) {
                                    if (mLightOpenList.size() < 4) {
                                        mLightOpenList.add(aLight);
                                        mLightOpenList.add(bLight);
                                    } else {
                                        if (isSixPoint == 5) {
                                            mLightOpenList.add(bLight);
                                        } else if (isSixPoint == 6) {
                                            mLightOpenList.add(aLight);
                                            mLightOpenList.add(bLight);
                                        }
                                    }
                                    mLightOnAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (mLightCloseList.size() < isSixPoint) {
                                    if (mLightCloseList.size() < 4) {
                                        mLightCloseList.add(aLight);
                                        mLightCloseList.add(bLight);
                                    } else {
                                        if (isSixPoint == 5) {
                                            mLightCloseList.add(bLight);
                                        } else if (isSixPoint == 6) {
                                            mLightCloseList.add(aLight);
                                            mLightCloseList.add(bLight);
                                        }
                                    }
                                    mLightOffAdapter.notifyDataSetChanged();
                                }
                            }
                            synchronized (obj) {
                                isDistance = true;
                            }
                            sendData();
                        }
                    }
                    mLightA.setText(mLightData[0] + "");
                    mLightB.setText(mLightData[1] + "");
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
        Intent intent = new Intent(context, LightActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mLightOnAdapter = new LightDataAdapter(mLightOpenList);
        mLightOffAdapter = new LightDataAdapter(mLightCloseList);
        mLightOnDragCallback = new ItemDragAndSwipeCallback(mLightOnAdapter);
        mLightOffDragCallback = new ItemDragAndSwipeCallback(mLightOffAdapter);
        mLightOnHelper = new ItemTouchHelper(mLightOnDragCallback);
        mLightOffHelper = new ItemTouchHelper(mLightOffDragCallback);
        mLightOnHelper.attachToRecyclerView(mLightOpenRecyclerview);
        mLightOffHelper.attachToRecyclerView(mLightCloseRecyclerview);

        mLightOnDragCallback.setSwipeMoveFlags(ItemTouchHelper.START);
        mLightOffDragCallback.setSwipeMoveFlags(ItemTouchHelper.START);
        mLightOnAdapter.enableSwipeItem();
        mLightOffAdapter.enableSwipeItem();
        mLightOpenRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mLightCloseRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mLightOpenRecyclerview.setAdapter(mLightOnAdapter);
        mLightCloseRecyclerview.setAdapter(mLightOffAdapter);
        //开关灯切换监听
        mLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isLamp = false;
                } else {
                    isLamp = true;
                }
            }
        });

        //弹窗设置
        mDialog = new PromptDialog(this);
        mDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration
                (3000);

        final PromptButton confirm = new PromptButton("6个点", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {
                isSixPoint = 6;
                mDialog.dismiss();
            }
        });
        confirm.setTextColor(Color.parseColor("#DAA520"));
        confirm.setFocusBacColor(Color.parseColor("#326EB4"));
        mDialog.showWarnAlert("请选择测试点数！", new PromptButton("5个点", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {
                isSixPoint = 5;
                mDialog.dismiss();
            }
        }), confirm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTcpServer.openConn(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_light;
    }

    @Override
    public String setToolbarTitle() {
        return "照度测试";
    }

    @Override
    public String setDataName(int type) {
        switch (type) {
            case AppConstant.LIGHT_A:
                return "LA";
            case AppConstant.LIGHT_B:
                return "LB";
        }
        return null;
    }

    @Override
    protected void sendRequestData(String equipNum) {
        if (isDistance) {
            mTcpServer.write(Util.getDistanceRequestData(equipNum));
        } else {
            mTcpServer.write(Util.getLightRequestData(equipNum));
        }
        mTcpServer.openConn(true);
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        synchronized (obj) {
            msg = Message.obtain();
            String sendData = new String(data);
            Log.e(TAG, "OnDataReceived: 获取到的数据" + sendData);
            if (sendData.contains("++")) {
                String subData = sendData.split("\\+\\+")[0];
                if (subData.contains("OK")) {
                    if (subData.substring(subData.indexOf("end") + 3, subData.indexOf("OK")).trim()
                            .equals("ZD")) {
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
                    mLightData = Util.getLightData(sendData);
                    for (int i = 0; i < 2; i++) {
                        sum += mLightData[i];
                    }
                    ave = ((sum / 2f) * 10) / 10f;
                    mLightData[3] = ave;
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
                for (int i = 1; i <= 6; i++) {
                    if (mLightOpenList.get(i-1) != null) {
                        dataMap.put("$O_" + i + "_1$", mLightOpenList.get(i)[0] + "");
                        dataMap.put("$O_" + i + "_2$", mLightOpenList.get(i)[1] + "");
                        dataMap.put("$O_" + i + "_3$", mLightOpenList.get(i)[2] + "");
                        dataMap.put("$O_" + i + "_ave$", mLightOpenList.get(i)[3] + "");
                    } else {
                        dataMap.put("$O_" + i + "_1$", "");
                        dataMap.put("$O_" + i + "_2$", "");
                        dataMap.put("$O_" + i + "_3$", "");
                        dataMap.put("$O_" + i + "_ave$", "");
                    }
                    if (mLightCloseList.get(i-1) != null) {
                        dataMap.put("$C_" + i + "_1$", mLightCloseList.get(i)[0] + "");
                        dataMap.put("$C_" + i + "_2$", mLightCloseList.get(i)[1] + "");
                        dataMap.put("$C_" + i + "_3$", mLightCloseList.get(i)[2] + "");
                        dataMap.put("$C_" + i + "_ave$", mLightCloseList.get(i)[3] + "");
                    } else {
                        dataMap.put("$C_" + i + "_1$", "");
                        dataMap.put("$C_" + i + "_2$", "");
                        dataMap.put("$C_" + i + "_3$", "");
                        dataMap.put("$C_" + i + "_ave$", "");
                    }
                }
                Gson gson = new Gson();
                Util.saveToDocData(AppConstant.LIGHT_DATA,gson.toJson(dataMap));
                TastyToast.makeText(getApplicationContext(), "数据保存成功", TastyToast.LENGTH_SHORT,
                        TastyToast.INFO);
                break;
        }
    }
}
