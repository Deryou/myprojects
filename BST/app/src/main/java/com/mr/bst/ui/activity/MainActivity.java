package com.mr.bst.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mr.bst.R;
import com.mr.bst.bean.ReportFile;
import com.mr.bst.callback.ServerConnCallback;
import com.mr.bst.gloable.AppConstant;
import com.mr.bst.util.TcpClient;
import com.mr.bst.util.TcpServer;
import com.mr.bst.util.Util;
import com.mr.bst.wifitools.HotspotManager;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mr.bst.util.Util.getHotspotName;
import static com.mr.bst.util.Util.getHotspotPwd;

public class MainActivity extends BaseActivity implements ServerConnCallback{

    @BindView(R.id.equip_setting)
    Button mEquipSetting;
    @BindView(R.id.equip_correct)
    Button mEquipCorrect;
    @BindView(R.id.temp_press_test)
    Button mTempPressTest;
    @BindView(R.id.shake_test)
    Button mShakeTest;
    @BindView(R.id.light_test)
    Button mLightTest;
    @BindView(R.id.flow_test)
    Button mFlowTest;

    private HotspotManager mHotspotManager;

    private AlertDialog mSetHotspotDialog;
    private LinearLayout mHotspotSetting;
    private EditText mHotspotName;
    private EditText mHotspotPwd;
    private TextView mHotspotTitle;
    private Button dialogConfirm,dialogCancel;
    //热点名称与密码
    private String ssid,passwd;
    private TcpServer mTcpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setNavigationIcon(null);
        initData();
        setDialog();
    }

    private void initData() {
        mHotspotManager = new HotspotManager(this);
        ssid = getHotspotName();
        if (isEmptyString(ssid)) {
            ssid = "MR-BST";
        }
        passwd = getHotspotPwd();
        if (isEmptyString(passwd)) {
            passwd = "123456789";
        }
        Util.saveHotspotData(ssid,passwd);

        mTcpServer = TcpServer.getInstance(AppConstant.HOTSPOT_PORT);
        mTcpServer.setServerConnCallback(this);
        mTcpServer.connect();
    }

    private void setDialog() {
        mHotspotSetting = (LinearLayout) getLayoutInflater().inflate(R.layout.hotspot_setting, null);
        mHotspotTitle = (TextView) mHotspotSetting.findViewById(R.id.dialog_title);
        mHotspotName = (EditText) mHotspotSetting.findViewById(R.id.hotspot_name);
        mHotspotPwd = (EditText) mHotspotSetting.findViewById(R.id.hotspot_pwd);
        dialogConfirm = (Button) mHotspotSetting.findViewById(R.id.dialog_confirm);
        dialogCancel = (Button) mHotspotSetting.findViewById(R.id.dialog_cancel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String setToolbarTitle() {
        return "多参数终端";
    }

    @OnClick({R.id.equip_setting, R.id.equip_correct, R.id.temp_press_test,R.id.shake_test, R.id
            .light_test, R.id
            .flow_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.equip_setting:
                EquipSetActivity.startActivity(this);
                break;
            case R.id.equip_correct:
                EquipCalibrateActivity.startActivity(this);
                break;
            case R.id.temp_press_test:
                TPActivity.startActivity(this);
                break;
            case R.id.shake_test:
                ShakeActivity.startActivity(this);
                break;
            case R.id.light_test:
                LightActivity.startActivity(this);
                break;
            case R.id.flow_test:
                FlowActivity.startActivity(this);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_file:
                ReportFileActivity.startActivity(this);
                break;
            case R.id.save_file:
                break;
            case R.id.hotspot_set:
                if (!isEmptyString(getHotspotName())) {
                    mHotspotName.setText(getHotspotName());
                } else {
                    mHotspotName.setText("");
                }
                if (!isEmptyString(getHotspotPwd())) {
                    mHotspotPwd.setText(getHotspotPwd());
                } else {
                    mHotspotPwd.setText("");
                }
                TastyToast.makeText(getApplicationContext(), "用户" + getHotspotName() + "密码"
                        +
                        getHotspotPwd(), TastyToast.LENGTH_LONG, TastyToast.INFO);
                mHotspotTitle.setText(getString(R.string.hotspot_dialog_title));
                if (mSetHotspotDialog == null) {
                    mSetHotspotDialog = new AlertDialog.Builder(this)
                            .setView(mHotspotSetting)
                            .create();
                    dialogConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = mHotspotName.getText().toString();
                            String pwd = mHotspotPwd.getText().toString();
                            if (isEmptyString(name) || isEmptyString(pwd)) {
                                TastyToast.makeText(getApplicationContext(), getString(R.string
                                        .hotspot_tip_notnull), TastyToast.LENGTH_LONG, TastyToast
                                        .WARNING);
                            }
                            Util.saveHotspotData(name, pwd);
                            if (getHotspotName().equals(name) && getHotspotPwd().equals
                                    (pwd)) {
                                ssid = name;
                                passwd = pwd;
                                mHotspotManager.openHotspot(ssid, passwd);
                                TastyToast.makeText(getApplicationContext(), getString(R.string
                                        .hotspot_tip_okandopen), TastyToast.LENGTH_LONG, TastyToast
                                        .SUCCESS);
                            } else {
                                TastyToast.makeText(getApplicationContext(), getString(R.string
                                        .hotspot_modifier_fail), TastyToast.LENGTH_LONG, TastyToast
                                        .ERROR);
                            }
                            mSetHotspotDialog.dismiss();
                        }
                    });
                    dialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSetHotspotDialog.dismiss();
                        }
                    });
                }
                mSetHotspotDialog.show();
                break;
            case R.id.hotspot_open:
                if (mHotspotManager == null) {
                    mHotspotManager = new HotspotManager(MainActivity.this);
                }
                if (mHotspotManager.openHotspot(ssid, passwd)) {
                    TastyToast.makeText(getApplicationContext(), getString(R.string
                            .hotspot_open_success), TastyToast.LENGTH_LONG, TastyToast
                            .SUCCESS);
                } else {
                    TastyToast.makeText(getApplicationContext(), getString(R.string
                            .hotspot_open_fail), TastyToast.LENGTH_LONG, TastyToast
                            .ERROR);
                }
                break;
            case R.id.hotspot_close:
                if (mHotspotManager == null) {
                    mHotspotManager = new HotspotManager(MainActivity.this);
                }
                mHotspotManager.closeHotspot();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mTcpServer.disconnect();
        super.onDestroy();
    }

    @Override
    public void ClientConnected(TcpClient client) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TastyToast.makeText(getApplicationContext(), "设备已连接", TastyToast.LENGTH_SHORT, TastyToast
                        .INFO);
            }
        });
    }
}
