package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mr.bst.R;
import com.mr.bst.adapter.WifiListAdapter;
import com.mr.bst.bean.WifiItem;
import com.mr.bst.callback.OnWifiPasswordConfirmCallback;
import com.mr.bst.callback.ServerCallback;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.receiver.WifiBroadcastReceiver;
import com.mr.bst.util.TcpClient;
import com.mr.bst.util.TcpServer;
import com.mr.bst.util.Util;
import com.mr.bst.wifitools.HotspotManager;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EquipSetActivity extends BaseActivity implements TcpClient.ConnectCallback,
        SwipeRefreshLayout.OnRefreshListener, ServerCallback {
    @BindView(R.id.wifi_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.wifi_switch)
    SwitchCompat mWifiSwitch;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mRefreshLayout;

    /**
     * 当前所选WiFi的SSID
     */
    private String mSelectedSSID;
    private WifiListAdapter mWifiAdapter;
    private List<WifiItem> mWifiItems;
    private WifiItem wifiItem;
    private HotspotManager mHotspotManager;
    private TcpClient mTcpClient = null;

    private AlertDialog mDialog;
    private AlertDialog mEnterPwdDialog;
    private LinearLayout mDialogLayout;
    private EditText mInputData;
    private Button mDialogConfirm, mDialogCancel;
    private TextView mDialogTitle, mDialogDisplay;

    private WifiBroadcastReceiver mWifiBroadcastReceiver = new WifiBroadcastReceiver() {
        @Override
        public void onWifiEnabled() {
            mHotspotManager.startScan();
        }

        @Override
        public void onWifiDisabled() {
            //wifi已关闭，清除可用wifi列表
            mSelectedSSID = "";
            mWifiItems.clear();
            mRefreshLayout.setRefreshing(false);
            mWifiAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanResultsAvailable(List<ScanResult> scanResults) {
            //扫描周围可用wifi成功，设置可用wifi列表
            mWifiItems.clear();
            for (int i = 0; i < scanResults.size(); i++) {
                wifiItem = new WifiItem();
                mWifiItems.add(wifiItem.setConn(false).setScanResult(scanResults.get(i)));
            }
            mRefreshLayout.setRefreshing(false);
            mWifiAdapter.notifyDataSetChanged();
        }

        @Override
        public void onWifiConnected(String connectedSSID) {
            onWifiChanged(connectedSSID);
        }

        @Override
        public void onWifiDisconnected() {

        }
    };

    public void onWifiChanged(String connectedSSID) {
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
        mWifiAdapter.notifyDataSetChanged();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EquipSetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置toolbar标题
        mToolbar.setTitle(getString(R.string.equip_setting));
        initData();
        initDialog();
        initAdapter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_equip_set;
    }

    @Override
    public String setToolbarTitle() {
        return "设备配置";
    }

    private void initDialog() {
        mDialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_single_input,
                null);
        mDialogTitle = (TextView) mDialogLayout.findViewById(R.id.dialog_title);
        mDialogDisplay = (TextView) mDialogLayout.findViewById(R.id.dialog_display);
        mInputData = (EditText) mDialogLayout.findViewById(R.id.dialog_input);
        mDialogConfirm = (Button) mDialogLayout.findViewById(R.id.dialog_confirm);
        mDialogCancel = (Button) mDialogLayout.findViewById(R.id.dialog_cancel);
    }

    private void initAdapter() {
        mWifiAdapter = new WifiListAdapter(R.layout.item_wifi_conn, mWifiItems);
        mWifiAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mWifiAdapter);
        mWifiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position < mWifiAdapter.getItemCount() && position >= 0) {
                    //获取当前wifi的SSID
                    final ScanResult scanResult = mWifiAdapter.getData().get(position).getScanResult();
                    mSelectedSSID = scanResult.SSID;
                    if (mHotspotManager.getConnectedSSID().equals(mSelectedSSID)) {
                        TastyToast.makeText(getApplicationContext(), getString(R.string
                                        .wifi_connected),
                                TastyToast
                                        .LENGTH_LONG, TastyToast.INFO);
                        return;
                    }

                    if (scanResult.capabilities != null && !(scanResult.capabilities.equals
                            (HotspotManager
                                    .NO_PASSWORD) || scanResult.capabilities.equals
                            (HotspotManager
                                    .NO_PASSWORD_ESS)
                            || scanResult.capabilities.equals(HotspotManager.NO_PASSWORD_WPS)
                    )) {
                        if (mHotspotManager.isConnectedStatue(scanResult)) {
                            mHotspotManager.connectToConfiguredNet(scanResult);
                            onWifiChanged(mSelectedSSID);
                        } else {
                            //弹出密码输入框
                            showDialogWithEditText(mSelectedSSID, new
                                    OnWifiPasswordConfirmCallback() {
                                        @Override
                                        public void onConfirm(String password) {
                                            //使用密码连接Wifi
                                            if (!TextUtils.isEmpty(password)) {
                                                try {
                                                    if (mHotspotManager.connectToNet(password,scanResult)) {
                                                        TastyToast.makeText(getApplicationContext(),
                                                                getString(R.string
                                                                        .wifi_connected_success),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.SUCCESS);
                                                    } else {
                                                        TastyToast.makeText(getApplicationContext(),
                                                                getString(R.string
                                                                        .wifi_connected_error),
                                                                TastyToast.LENGTH_LONG,
                                                                TastyToast.ERROR);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                TastyToast.makeText(getApplicationContext(), getString(R
                                                                .string.tip_pwd_empty), TastyToast
                                                                .LENGTH_LONG,
                                                        TastyToast.WARNING);
                                            }
                                        }
                                    });
                        }
                    } else {
                        //连接免密码Wifi
                        try {
                            if (mHotspotManager.connectWifi(mSelectedSSID, "", mWifiItems)) {
                                TastyToast.makeText(getApplicationContext(), getString(R.string
                                                .wifi_connected_success), TastyToast.LENGTH_LONG,
                                        TastyToast.SUCCESS);
                                onWifiChanged(mSelectedSSID);
                            } else {
                                TastyToast.makeText(getApplicationContext(), getString(R.string
                                                .wifi_connected_error), TastyToast.LENGTH_LONG,
                                        TastyToast.ERROR);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    protected void showDialogWithEditText(String title, final OnWifiPasswordConfirmCallback
            callback) {
        mDialogTitle.setText(getString(R.string.dialog_enter_pwd_title));
        mDialogDisplay.setText(getString(R.string.dialog_enter_pwd_display));
        if (mEnterPwdDialog == null) {
            mEnterPwdDialog = new AlertDialog.Builder(this)
                    .setView(mDialogLayout)
                    .create();
            mDialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onConfirm(mInputData.getText().toString().trim());
                    }
                    mEnterPwdDialog.dismiss();
                }
            });
            mDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEnterPwdDialog.dismiss();
                }
            });
        }
        mEnterPwdDialog.show();
    }

    private void initData() {
        mWifiItems = new ArrayList<>();
        mHotspotManager = new HotspotManager(this);
        mWifiSwitch.setChecked(true);
        mWifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWifiSwitch.isChecked()) {
                    mRefreshLayout.setRefreshing(true);
                    mHotspotManager.openWifi();
                } else {
                    mHotspotManager.closeWifi();
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(Color.rgb(82, 155, 234));
        mRefreshLayout.setRefreshing(true);

        //设备连接
        TcpServer.getInstance(AppConstant.HOTSPOT_PORT).setServerCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerWifiReceiver();
        if (mHotspotManager.isHotspotOn()) {
            mHotspotManager.closeHotspot();
        }
        if (mHotspotManager.isWifiEnabled()) {
            mHotspotManager.startScan();
        } else {
            mHotspotManager.openWifi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //界面不可见时解除wifi广播注册
        unregisterWifiReceiver();
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
    protected void onDestroy() {
        if (mTcpClient != null) {
            mTcpClient.disconnect();
        }
        super.onDestroy();
    }

    private void connectEquipment() {
        mTcpClient = new TcpClient(AppConstant.REMOTE_IP, AppConstant.REMOTE_PORT);
        mTcpClient.setConnectCallback(this);
        mTcpClient.connect();
    }

    @Override
    public void onConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TastyToast.makeText(getApplicationContext(), getString(R.string
                                .equip_conn_successed),
                        TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
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
                mDialogTitle.setText(getString(R.string.dialog_title_set_queipnum));
                mDialogDisplay.setText(getString(R.string.dialog_display_set_queipnum));
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(this)
                            .setView(mDialogLayout)
                            .create();
                    mDialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String equipNum = mInputData.getText().toString();
                            if (equipNum == null) {
                                TastyToast.makeText(getApplicationContext(), getString(R.string
                                        .warn_enter_number), TastyToast.LENGTH_LONG, TastyToast
                                        .WARNING);
                                return;
                            }
                            final String[] data = Util.getSendData(Integer.valueOf(equipNum));
                            if (data.length > 0 && mTcpClient != null) {
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        for (int i = 0; i < data.length; i++) {
                                            if (!mTcpClient.write(data[i].getBytes())) {
                                                TastyToast.makeText(getApplicationContext(),
                                                        getString
                                                                (R.string.write_to_equip_failed),
                                                        TastyToast
                                                                .LENGTH_LONG, TastyToast.ERROR);
                                                return;
                                            }
                                            try {
                                                Thread.sleep(150);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        mHotspotManager.openHotspot(Util.getHotspotName(), Util
                                                .getHotspotPwd());
                                        Util.saveEquipNum(equipNum);
                                        TastyToast.makeText(getApplicationContext(), getString(R
                                                .string.write_to_equip_success), TastyToast
                                                .LENGTH_LONG, TastyToast.SUCCESS);
                                        finish();
                                        try {
                                            mTcpClient.disconnect();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            EquipSetActivity.this.finish();
                                        }
                                        Looper.loop();
                                    }
                                };
                                new Thread(runnable).start();
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
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectFail(String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TastyToast.makeText(getApplicationContext(), getString(R.string.equip_conn_error),
                        TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    @Override
    public void onDataReceived(byte[] data, int available) {

    }

    @Override
    public void onRefresh() {
        mWifiAdapter.setEnableLoadMore(false);
        if (mHotspotManager.isWifiEnabled()) {
            mHotspotManager.startScan();
        }
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        String ensureData = new String(data);
        if (ensureData.equals("SOKW")) {
            TastyToast.makeText(getApplicationContext(), getString(R
                    .string.write_to_equip_success), TastyToast
                    .LENGTH_LONG, TastyToast.SUCCESS);
        }
    }
}
