package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mr.bst.R;
import com.mr.bst.adapter.ViewPagerAdapter;
import com.mr.bst.callback.PressCallback;
import com.mr.bst.callback.ServerCallback;
import com.mr.bst.callback.TempCallback;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.ui.fragment.TpPressureFragment;
import com.mr.bst.ui.fragment.TpShakeFragment;
import com.mr.bst.ui.fragment.TpTempFragment;
import com.mr.bst.util.TcpClient;
import com.mr.bst.util.TcpServer;
import com.mr.bst.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TPActivity extends BaseActivity implements ServerCallback {
    private static final String TAG = "TPActivity";

    @BindView(R.id.tp_tablayout)
    TabLayout mTpTablayout;
    @BindView(R.id.tp_viewpager)
    ViewPager mTpViewpager;
    @BindView(R.id.now_temp)
    EditText mNowTemp;
    @BindView(R.id.now_pressure)
    EditText mNowPressure;
    Message msg;
    @BindView(R.id.recode_data)
    Button mRecodeData;
    @BindView(R.id.before_temp)
    Button mBeforeTemp;
    @BindView(R.id.diff_press)
    EditText mDiffPress;
    @BindView(R.id.press_ave)
    EditText mPressAve;
    @BindView(R.id.start_temp)
    EditText mStartTemp;
    @BindView(R.id.after_temp)
    EditText mAfterTemp;
    @BindView(R.id.raised_temp)
    EditText mRaisedTemp;
    float nowTemp = 0;
    float press_ave = 0;
    float temp_raised = 0;
    private List<Fragment> mFragments;
    private Fragment mFragment;
    private TempCallback mTempCallback;
    private PressCallback mPressCallback;
    private ViewPagerAdapter mViewPagerAdapter;
    private TcpServer mTcpServer;
    private Thread writeThread;
    //接收到的数据
    private float[] obtainData = new float[3];
    private float dataOne;
    private float[] dataPress = new float[3];
    private boolean isRecode = false;
    private int count = 0;
    private float sum = 0;
    private String pressData = "";
    private float mBootBData = 0;

    private HashMap<String,String> dataMap;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TastyToast.makeText(getApplicationContext(), "温压数据请求成功", TastyToast
                            .LENGTH_LONG, TastyToast.SUCCESS);
                    break;
                case 1:
                    if (!(mFragment instanceof TpShakeFragment)) {
                        mTempCallback.setData(obtainData);
                        mPressCallback.setData(obtainData);
                    }
                    if (isRecode && count < 3) {
                        dataPress[count] = obtainData[AppConstant.TP_PRESSURE];
                        sum += dataPress[count];
                        if (count == 2) {
                            pressData = pressData + dataPress[count];
                            mDiffPress.setText(pressData);
                            press_ave = Math.round((sum / 3f) * 100) / 100f;
                            mPressAve.setText(press_ave + "");
                        } else {
                            pressData = pressData + dataPress[count] + ", ";
                        }
                        count++;
                    }
                    break;
            }
            if (obtainData != null) {
                mNowTemp.setText(obtainData[AppConstant.TP_TEMP] + "");
                mNowPressure.setText(obtainData[AppConstant.TP_PRESSURE] + "");
            }
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TPActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTcpServer.openConn(false);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(TpTempFragment.newInstance());
        mFragments.add(TpPressureFragment.newInstance());
        mFragment = mFragments.get(0);
        mTempCallback = (TempCallback) mFragments.get(0);
        mPressCallback = (PressCallback) mFragments.get(1);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        mTpViewpager.setOffscreenPageLimit(3);
        mTpViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFragment = mFragments.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTpViewpager.setAdapter(mViewPagerAdapter);
        mTpTablayout.setupWithViewPager(mTpViewpager);

        //设置数据监听
        mTcpServer = TcpServer.getInstance(AppConstant.HOTSPOT_PORT);
        mTcpServer.setServerCallback(this);

        sendReqData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tp;
    }

    @Override
    public String setToolbarTitle() {
        return "温压测试";
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.tp_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.data_refresh:
//                sendReqData();
//                break;
//            case R.id.data_clear:
//                if (mFragment instanceof TpTempFragment) {
//                    ((TpTempFragment) mFragment).clearData();
//                } else if (mFragment instanceof TpPressureFragment) {
//                    ((TpPressureFragment) mFragment).clearData();
//                }
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendReqData() {
        final String tpNum = Util.getEquipNum();
        if (writeThread != null) {
            writeThread.interrupt();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (tpNum != null) {
                    mTcpServer.write(Util.getTPRequestData(tpNum));
                    mTcpServer.openConn(true);
                }
            }
        };
        writeThread = new Thread(runnable);
        writeThread.start();
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        msg = Message.obtain();
        String sendData = new String(data);
        if (sendData.contains("++")) {
            String subData = sendData.split("\\+\\+")[0];
            if (subData.substring(subData.indexOf("end") + 3, subData.indexOf("OK")).trim()
                    .equals("WP")) {
                msg.what = 0;
            }
        } else {
            obtainData = Util.getTPData(sendData);

            msg.what = 1;
        }
        mHandler.sendMessage(msg);
    }

    @OnClick({R.id.before_temp, R.id.recode_data, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.before_temp:
//                mBundle.putFloat("start_temp",obtainData[AppConstant.TP_TEMP]);
                mBootBData = obtainData[AppConstant.TP_TEMP];
                mStartTemp.setText(mBootBData + "");
                mBeforeTemp.setVisibility(View.GONE);
                mRecodeData.setVisibility(View.VISIBLE);
                break;
            case R.id.recode_data:
//                mBundle.putFloat("after_temp",obtainData[AppConstant.TP_TEMP]);
                count = 0;
                pressData = "";
                sum = 0;
                temp_raised = 0;
                press_ave = 0;
                isRecode = true;
                nowTemp = obtainData[AppConstant.TP_TEMP];
                mAfterTemp.setText(nowTemp + "");
                temp_raised = Math.round((nowTemp - mBootBData) * 10) / 10f;
                mRaisedTemp.setText(temp_raised + "");
                break;
            case R.id.save_btn:
                dataMap = new HashMap<>();
                dataMap.put("$PA_1$", dataPress[0]+"");
                dataMap.put("$PA_2$", dataPress[1]+"");
                dataMap.put("$PA_3$", dataPress[2]+"");
                dataMap.put("$PA_ave$", press_ave+"");
                dataMap.put("$OBT$", mBootBData + "");
                dataMap.put("$OAT$", nowTemp + "");
                dataMap.put("$T_ave$", temp_raised + "");
                Gson gson = new Gson();
                String tp_data = gson.toJson(dataMap);
                Util.saveToDocData(AppConstant.TP_DATA,tp_data);
                TastyToast.makeText(getApplicationContext(), "数据保存成功", TastyToast.LENGTH_SHORT,
                        TastyToast.INFO);
                break;
        }
    }
}
