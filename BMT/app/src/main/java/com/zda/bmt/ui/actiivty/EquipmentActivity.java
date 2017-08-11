package com.zda.bmt.ui.actiivty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.sdsmdg.tastytoast.TastyToast;
import com.zda.bmt.R;
import com.zda.bmt.bean.BottomTabData;
import com.zda.bmt.bean.DataBean;
import com.zda.bmt.callback.EquimentCallback;
import com.zda.bmt.callback.HumidtyCallback;
import com.zda.bmt.callback.OxygenCallback;
import com.zda.bmt.callback.ServerCallback;
import com.zda.bmt.callback.TemperatureCallback;
import com.zda.bmt.gloable.AppConstant;
import com.zda.bmt.util.TcpClient;
import com.zda.bmt.util.TcpServer;
import com.zda.bmt.util.Util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class EquipmentActivity extends BaseActivity implements ServerCallback {
    private static final String TAG = "EquipmentActivity";
    public TcpServer mTcpServer;
    //当前的fragment
    protected Fragment currentFragment = null;
    @BindView(R.id.time_gap)
    EditText mTimeGap;
    @BindView(R.id.type_32)
    RadioButton mType32;
    @BindView(R.id.type_36)
    RadioButton mType36;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.top_area)
    LinearLayout mTopArea;
    @BindView(R.id.home_container)
    FrameLayout mHomeContainer;
    @BindView(R.id.bottom_tab)
    TabLayout mBottomTab;
    Message msg;
    private Map<String, String> dataMap = new HashMap<>();
    private List<DataBean> dataList;
    //所有的fragment集合
    private Fragment[] mFragments;
    private float ta, tb, tc, td, te, temp, humdityData, oxygenData;
    private boolean isConnected = false;
    private boolean isStart = false;
    private boolean isType_32 = true;
    private int count = 1;
    private String tempType = "";
    private AlertDialog mDialog;
    private EditText mFileName;

    private EquimentCallback mEquimentCallback;
    private TemperatureCallback mTemperatureCallback;
    private HumidtyCallback mHumidtyCallback;
    private OxygenCallback mOxygenCallback;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isStart && dataList.get(0).count < 15) {
                        dataList.get(0).count++;
                        dataList.get(0).data.add(ta);
                        dataMap.put("$ST" + tempType + (dataList.get(0).count + 1) + "$",
                                mTemperatureCallback
                                        .getInputData());
                        dataList.get(7).data.add(Float.valueOf(mTemperatureCallback.getInputData
                                ()));
                        if (dataList.get(0).count == 15) {
                            dataMap.put("$ST" + tempType + "AVE$", String.valueOf(Util
                                    .getListAve
                                            (dataList.get(7).data)));
                            TastyToast.makeText(getApplicationContext(), "数据收集完毕", TastyToast
                                    .LENGTH_SHORT, TastyToast.SUCCESS);
                            setEditState(true);
                        }
                        count++;
                    }
                    mTemperatureCallback.setData(ta, 0);
                    break;
                case 1:
                    if (isStart && dataList.get(1).count < 15) {
                        dataList.get(1).count++;
                        dataList.get(1).data.add(tb);
                    }
                    mTemperatureCallback.setData(tb, 1);
                    break;
                case 2:
                    if (isStart && dataList.get(2).count < 15) {
                        dataList.get(2).count++;
                        dataList.get(2).data.add(tc);
                    }
                    mTemperatureCallback.setData(tc, 2);
                    break;
                case 3:
                    if (isStart && dataList.get(3).count < 15) {
                        dataList.get(3).count++;
                        dataList.get(3).data.add(td);
                    }
                    mTemperatureCallback.setData(td, 3);
                    break;
                case 4:
                    if (isStart && dataList.get(4).count < 15) {
                        dataList.get(4).count++;
                        dataList.get(4).data.add(te);
                    }
                    mTemperatureCallback.setData(te, 4);
                    break;
                case 5:
                    if (isStart && dataList.get(5).count < 3) {
                        dataList.get(5).count++;
                        dataList.get(5).data.add(humdityData);
                        dataMap.put("$SH0" + (dataList.get(5).count + 1) + "$", mHumidtyCallback
                                .getInputData());
                        dataList.get(8).data.add(Float.valueOf(mHumidtyCallback.getInputData()));
                        if (dataList.get(5).count == 3) {
                            dataMap.put("$SH_AVE$", String.valueOf(Util.getListAve(dataList.get
                                    (8).data)));
                        }
                    }
                    mHumidtyCallback.setData(humdityData);
                    break;
                case 6:
                    if (isStart && dataList.get(6).count < 3) {
                        dataList.get(6).count++;
                        dataList.get(6).data.add(humdityData);
                        dataMap.put("$SO0" + (dataList.get(6).count + 1) + "$", mHumidtyCallback
                                .getInputData());
                        dataList.get(9).data.add(Float.valueOf(mHumidtyCallback.getInputData()));
                        if (dataList.get(6).count == 3) {
                            dataMap.put("$SO_AVE$", String.valueOf(Util.getListAve(dataList.get
                                    (9).data)));
                        }
                    }
                    mOxygenCallback.setData(oxygenData);
                    break;
            }
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EquipmentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentData();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_file:
                ReportFileActivity.startActivity(this);
                break;
            case R.id.save_doc:
                String dataTime = Util.getNowTime();
                mFileName.setText(dataTime);
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(this).setTitle("请输入文件名称")
                            .setView(R.layout.input_filename_layout)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputStream inputStream = getAssets().open("report.doc");
                                        String demoPath = Util.getDemoDocPath(EquipmentActivity
                                                .this)
                                                + "/bmt_report_file.doc";
                                        String reportPath = Util.getSDPath(EquipmentActivity.this) +
                                                AppConstant.REPORT_FOLDER + mFileName.getText().toString()
                                                + ".doc";
                                        File demoFile = new File(demoPath);
                                        File reportFile = new File(reportPath);
                                        if (Util.writeToFile(demoFile, inputStream, false)) {
                                            writeDataToDoc(demoFile, reportFile);
                                            TastyToast.makeText(getApplicationContext(), "保存成功！",
                                                    TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                        } else {
                                            TastyToast.makeText(getApplicationContext(), "文件写入失败！",
                                                    TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setNegativeButton("取消", null)
                            .create();
                }
                mDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 把数据写入到文档
     *
     * @param demoFile
     * @param reportFile
     */
    private void writeDataToDoc(File demoFile, File reportFile) {
        dataMap.putAll(mEquimentCallback.getEquipData());
        String type = "";
        for (int i = 0; i < 5; i++) {
            if (dataList.get(i).data != null) {
                for (int j = 0; j < 15; j++) {
                    switch (i) {
                        case 0:
                            type = "A";
                            break;
                        case 1:
                            type = "B";
                            break;
                        case 2:
                            type = "C";
                            break;
                        case 3:
                            type = "D";
                            break;
                        case 4:
                            type = "E";
                            break;
                    }
                    dataMap.put("$T" + type + tempType + (i + 1) + "$", String.valueOf(dataList
                            .get(i)
                            .data.size() < 1 || dataList
                            .get(i)
                            .data.size()<=j ? "" : dataList
                            .get(i)
                            .data.get(j)));
                }
            }
            dataMap.put("$T" + type + tempType + "AVE$", String.valueOf(Util.getListAve
                    (dataList.get(i).data)));
        }
        for (int i = 0; i < 3; i++) {
            dataMap.put("$TH0" + (i + 1) + "$", String.valueOf(dataList.get(8).data.size()
                    < 1 || dataList.get(8).data.size()<=i ? "" : dataList
                    .get(8).data.get(i)));

            dataMap.put("$TO0" + (i + 1) + "$", String.valueOf(dataList.get(9).data.size()
                    < 1 || dataList.get(9).data.size()<=i ? "" : dataList
                    .get(9).data.get(i)));

            if (i == 2) {
                dataMap.put("$TH_AVE$", String.valueOf(Util.getListAve(dataList.get
                        (8).data)));

                dataMap.put("$TO_AVE$", String.valueOf(Util.getListAve(dataList.get
                        (9).data)));
            }
        }
        try {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            Range range = hdt.getRange();
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(reportFile);
            hdt.write(ostream);
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        mBottomTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab状态
                for (int i = 0; i < mBottomTab.getTabCount(); i++) {
                    if (i == tab.getPosition()) {
                        mBottomTab.getTabAt(i).setIcon(getResources().getDrawable(BottomTabData
                                .mTabResPressed[i]));
                    } else {
                        mBottomTab.getTabAt(i).setIcon(getResources().getDrawable(BottomTabData
                                .mTabRes[i]));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_form_select)).setText(BottomTabData.mTableTitle[0]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_temp_normal)).setText(BottomTabData.mTableTitle[1]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_humdity_normal)).setText(BottomTabData.mTableTitle[2]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_oxygen_normal)).setText(BottomTabData.mTableTitle[3]));

        mType32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tempType = AppConstant.TYPE_32;
                }
            }
        });
        mType36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tempType = AppConstant.TYPE_36;
                }
            }
        });

        //设置数据监听
        mTcpServer = TcpServer.getInstance(AppConstant.HOTSPOT_PORT);
        mTcpServer.setServerCallback(this);
        mTcpServer.openConn(true);

        //文件名EditText初始化
        mFileName = (EditText) getLayoutInflater().inflate(R.layout.input_filename_layout, null);
    }

    /**
     * 初始化fragment数据
     */
    private void initFragmentData() {
        dataList = new ArrayList<>();
        mFragments = BottomTabData.getFragments();
        mEquimentCallback = (EquimentCallback) mFragments[0];
        mTemperatureCallback = (TemperatureCallback) mFragments[1];
        mHumidtyCallback = (HumidtyCallback) mFragments[2];
        mOxygenCallback = (OxygenCallback) mFragments[3];
        //初始添加实例对象
        for (int i = 0; i < 10; i++) {
            dataList.add(new DataBean());
        }
    }

    private void onTabItemSelected(int position) {

        switch (position) {
            case 0:
                currentFragment = mFragments[0];
                mTopArea.setVisibility(View.GONE);
                break;
            case 1:
                currentFragment = mFragments[1];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            case 2:
                currentFragment = mFragments[2];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            case 3:
                currentFragment = mFragments[3];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (currentFragment != null) {
            for (Fragment fragment : mFragments) {
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
            if (!currentFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(R.id.home_container,
                        currentFragment).commit();
            }
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_equipment;
    }

    @Override
    public String setToolbarTitle() {
        return "数据采集";
    }

    @Override
    protected void onDestroy() {
        mTcpServer.openConn(false);
        super.onDestroy();
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        //OnDataReceived: CD022endST02Ta10685.00DL094end
//        CD018endST02Ha13.7DL083end
        if (client.mSocket.isConnected()) {
            isConnected = true;
        }
        String sendData = new String(data).trim();
        msg = mHandler.obtainMessage();
        Log.e(TAG, "OnDataReceived: " + sendData);

        if (sendData.endsWith("end")) {
            if (sendData.contains("H")) {
                humdityData = Util.getHumdityData(sendData);
                msg.what = 5;
            } else if (sendData.contains("O")) {
                oxygenData = Util.getOxygenData(sendData);
                msg.what = 6;
            } else {
                int type = Integer.parseInt(sendData.substring(sendData.indexOf("ST") + 2,
                        sendData.lastIndexOf
                                ("T")));
                temp = Util.getTempData(sendData);
                Log.e(TAG, "OnDataReceived: 温度");
                if (type == 1) {
                    ta = temp;
                    msg.what = 0;
                } else if (type == 2) {
                    tb = temp;
                    msg.what = 1;
                } else if (type == 3) {
                    tc = temp;
                    msg.what = 2;
                } else if (type == 4) {
                    td = temp;
                    msg.what = 3;
                } else if (type == 5) {
                    te = temp;
                    msg.what = 4;
                }
            }
        }
        mHandler.sendMessage(msg);
    }

    /**
     * 设置控件的状态
     *
     * @param state
     */
    public void setEditState(boolean state) {
        mTimeGap.setEnabled(state);
        mType32.setEnabled(state);
        mType36.setEnabled(state);
        mBtnStart.setEnabled(state);
    }

    /**
     * 数据清理
     *
     * @param dataBeen
     */
    public void resetData(List<DataBean> dataBeen) {
        for (DataBean data : dataBeen) {
            data.count = 0;
            data.data.clear();
        }
    }

    @OnClick(R.id.btn_start)
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (!isConnected) {
                    TastyToast.makeText(getApplicationContext(), "设备未连接", TastyToast
                            .LENGTH_SHORT, TastyToast.WARNING);
                    return;
                }
                TastyToast.makeText(getApplicationContext(), "开始采集数据", TastyToast.LENGTH_SHORT,
                        TastyToast.INFO);
                setEditState(false);
                isStart = true;
                break;
        }
    }
}
