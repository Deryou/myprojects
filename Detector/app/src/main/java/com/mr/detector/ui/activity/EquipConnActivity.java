package com.mr.detector.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mr.detector.R;
import com.mr.detector.adapter.BaseRecyclerViewAdapter;
import com.mr.detector.adapter.WifiListAdapter;
import com.mr.detector.adapter.holder.BaseHolder;
import com.mr.detector.bean.WifiItem;
import com.mr.detector.callback.OnWifiPasswordConfirmCallback;
import com.mr.detector.gloable.AppConstants;
import com.mr.detector.receiver.WifiBroadcastReceiver;
import com.mr.detector.util.DividerItemDecoration;
import com.mr.detector.util.TcpClient;
import com.mr.detector.util.Util;
import com.mr.detector.wifitools.HotspotManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 连接设备界面
 * 提供扫描到的wifi列表找到目标设备wifi进行连接配置
 */
public class EquipConnActivity extends BaseActivity implements BaseRecyclerViewAdapter
        .OnItemClickListener, TcpClient.ConnectCallback {
    private static final String TAG = "EquipConnActivity";
    public static boolean isWifiConnected;
    @BindView(R.id.wifi_recycler_view)
    RecyclerView mWifiRecycler;
    @BindView(R.id.wifi_switch)
    SwitchCompat mWifiSwitch;
    /**
     * 当前所选WiFi的SSID
     */
    private String mSelectedSSID;
    /**
     * 扫描到的可用WiFi列表
     */
    private List<WifiItem> mWifiItems = new ArrayList<>();
    private WifiItem wifiItem;
    private HotspotManager mManager;
    private WifiListAdapter mWifiListAdapter;
    //设备配置
    //tcp客户端,用于发送配置信息
    private TcpClient mClient = null;
    //设置设备号弹窗
    private AlertDialog mDialog;
    private LinearLayout mDialogLayout;
    private EditText mEquipNum;
    private Button mDialogConfirm, mDialogCancel;

    private WifiBroadcastReceiver mWifiBroadcastReceiver = new WifiBroadcastReceiver() {
        @Override
        public void onWifiEnabled() {
            mManager.startScan();
        }

        @Override
        public void onWifiDisabled() {
            //wifi已关闭，清除可用wifi列表
            mSelectedSSID = "";
            mWifiItems.clear();
            setWifiAdapter();
        }

        @Override
        public void onScanResultsAvailable(List<ScanResult> scanResults) {
            //扫描周围可用wifi成功，设置可用wifi列表
            mWifiItems.clear();
            for (int i = 0; i < scanResults.size(); i++) {
                wifiItem = new WifiItem();
                mWifiItems.add(wifiItem.setConn(false).setScanResult(scanResults.get(i)));
            }
            Log.e(TAG, "onScanResultsAvailable: " + mWifiItems.toString());
            setWifiAdapter();
        }

        @Override
        public void onWifiConnected(String connectedSSID) {
            //wifi连接成功后的一些处理
            WifiItem result;
            for (int i = 0; i < mWifiItems.size(); i++) {
                result = mWifiItems.get(i);
                if (result.getScanResult().SSID.equals(connectedSSID)) {
                    mWifiItems.get(i).setConn(true);
                } else {
                    mWifiItems.get(i).setConn(false);
                }
            }
            setWifiAdapter();
        }

        @Override
        public void onWifiDisconnected() {

        }
    };

    /**
     * 启动activity，可利用Intent传递数据
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EquipConnActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initDialog();
    }

    private void initDialog() {
        mDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_single_input,
                null);
        mEquipNum = (EditText) mDialogLayout.findViewById(R.id.dialog_input);
        mDialogConfirm = (Button) mDialogLayout.findViewById(R.id.dialog_confirm);
        mDialogCancel = (Button) mDialogLayout.findViewById(R.id.dialog_cancel);
    }

    @Override
    protected void onDestroy() {
        if (mClient != null) {
            mClient.disconnect();
        }
        super.onDestroy();
    }

    private void connectEquipment() {
        mClient = new TcpClient(AppConstants.REMOTE_IP, AppConstants.REMOTE_PORT);
        mClient.setConnectCallback(this);
        mClient.connect();
    }

    /**
     * 初始化wifi列表适配器
     */
    private void setWifiAdapter() {
        if (mWifiListAdapter == null) {
            mWifiRecycler.setLayoutManager(new LinearLayoutManager(this));
            mWifiRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                    .VERTICAL_LIST));
            mWifiListAdapter = new WifiListAdapter(this, mWifiItems);
            mWifiListAdapter.setOnItemClickListener(this);
            mWifiRecycler.setAdapter(mWifiListAdapter);
        } else {
            mWifiListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerWifiReceiver();
        if (mManager.isHotspotOn()) {
            mManager.closeHotspot();
        }
        if (mManager.isWifiEnabled()) {
            mManager.startScan();
        } else {
            mManager.openWifi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //界面不可见时解除wifi广播注册
        unregisterWifiReceiver();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mManager = new HotspotManager(this);
        if (!mManager.isWifiEnabled()) {
            mManager.openWifi();
        }
        mWifiSwitch.setChecked(true);
        mWifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWifiSwitch.isChecked()) {
                    mManager.openWifi();
                } else {
                    mManager.closeWifi();
                }
            }
        });
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        //设置toolbar返回按钮事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected String setToolbarTitle() {
        return "设备连接配置";
    }

    /**
     * wifi广播接收器
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_equip_conn;
    }

    protected void showDialogWithEditText(String title, final OnWifiPasswordConfirmCallback
            callback) {
        final EditText etPassword = (EditText) LayoutInflater.from(this).inflate(R.layout
                .dialog_with_edittext, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(etPassword)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            callback.onConfirm(etPassword.getText().toString().trim());
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
        builder.show();
    }

    /**
     * 注册监听广播
     */
    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mWifiBroadcastReceiver, filter);
    }

    /**
     * 取消注册
     */
    private void unregisterWifiReceiver() {
        if (mWifiBroadcastReceiver != null) {
            unregisterReceiver(mWifiBroadcastReceiver);
            mWifiBroadcastReceiver = null;
        }
    }

    @Override
    public void onItemClick(BaseHolder holder, int position) {
        if (position < mWifiListAdapter.getItemCount() && position >= 0) {
            //获取当前wifi的SSID
            ScanResult scanResult = mWifiListAdapter.getData().get(position).getScanResult();
            mSelectedSSID = scanResult.SSID;
            if (scanResult.capabilities != null && !(scanResult.capabilities.equals(HotspotManager
                    .NO_PASSWORD) || scanResult.capabilities.equals(HotspotManager.NO_PASSWORD_ESS)
                    || scanResult.capabilities.equals(HotspotManager.NO_PASSWORD_WPS))) {
                //弹出密码输入框
                showDialogWithEditText(mSelectedSSID, new OnWifiPasswordConfirmCallback() {
                    @Override
                    public void onConfirm(String password) {
                        //使用密码连接Wifi
                        if (!TextUtils.isEmpty(password)) {
                            try {
                                if (mManager.connectWifi(mSelectedSSID, password, mWifiItems)) {
                                    isWifiConnected = true;
                                } else {
                                    isWifiConnected = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            toast("密码不能为空");
                        }
                    }
                });
            } else {
                //连接免密码Wifi
                try {
                    if (mManager.connectWifi(mSelectedSSID, "", mWifiItems)) {
                        isWifiConnected = true;
                    } else {
                        isWifiConnected = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mWifiListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_setting:
                connectEquipment();
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(this)
                            .setView(mDialogLayout)
                            .create();
                    mDialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String equipNum = mEquipNum.getText().toString();
                            final String[] data = Util.getSendData(Integer.valueOf(equipNum));
                            if (data.length > 0 && mClient != null) {
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        for (int i = 0; i < data.length; i++) {
                                            if (!mClient.write(data[i].getBytes())) {
                                                toast("写入配置失败了");
                                                return;
                                            }
                                            try {
                                                Thread.sleep(150);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        mManager.openHotspot(Util.getHotspotName(), Util
                                                .getHotspotPwd());
                                        toast("写入配置成功");
                                        finish();
                                        try {
                                            mClient.disconnect();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            EquipConnActivity.this.finish();
                                        }
                                        Looper.loop();
                                    }
                                };
                                new Thread(runnable).start();
                            } else {
                                toast("mClient为空");
                            }
                            mDialog.dismiss();
                        }
                    });
                    mDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                }
                mDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast("连接设备成功");
            }
        });
    }

    @Override
    public void onConnectFail(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast(data);
            }
        });
    }

    @Override
    public void onDataReceived(byte[] data, int availabe) {

    }
}
