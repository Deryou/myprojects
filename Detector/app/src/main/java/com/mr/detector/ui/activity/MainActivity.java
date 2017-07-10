package com.mr.detector.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.mr.detector.R;
import com.mr.detector.util.Util;
import com.mr.detector.wifitools.HotspotManager;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

import static com.mr.detector.util.Util.getHotspotName;
import static com.mr.detector.util.Util.getHotspotPwd;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    @BindView(R.id.add_equipment)
    Button mAddEquipment;
    @BindView(R.id.conn_equipment)
    Button mConn;

    private AlertDialog mSetHotspotDialog;
    private TableLayout mHotspotSetting;
    private EditText mHotspotName;
    private EditText mHotspotPwd;
    //热点名称和密码
    private HotspotManager mManager;
    private String ssid;
    private String password;
    private PromptDialog mPromptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPromptDialog = new PromptDialog(this);
        mPromptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        setDialog();
        initData();
    }

    private void initData() {
        ssid = getHotspotName();
        if (isEmptyString(ssid)) {
            ssid = "MR";
        }
        password = getHotspotPwd();
        if (isEmptyString(password)) {
            password = "123456789";
        }
        Util.saveHotspotData(ssid,password);
    }

    @Override
    protected String setToolbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void setDialog() {
        mHotspotSetting = (TableLayout) getLayoutInflater().inflate(R.layout.hotspot_setting, null);
        mHotspotName = (EditText) mHotspotSetting.findViewById(R.id.hotspot_name);
        mHotspotPwd = (EditText) mHotspotSetting.findViewById(R.id.hotspot_pwd);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        mToolbar.setNavigationIcon(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hotspot_set:
                if (isNotEmptyString(getHotspotName())) {
                    mHotspotName.setText(getHotspotName());
                } else {
                    mHotspotName.setText("");
                }
                if (isNotEmptyString(getHotspotPwd())) {
                    mHotspotPwd.setText(getHotspotPwd());
                } else {
                    mHotspotName.setText("");
                }
                toast(getHotspotName() + "密码：" + getHotspotPwd());
                if (mSetHotspotDialog == null) {
                    mSetHotspotDialog = new AlertDialog.Builder(this)
                            .setTitle("更改热点名称和密码")
                            .setView(mHotspotSetting)
                            .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = mHotspotName.getText().toString();
                                    String pwd = mHotspotPwd.getText().toString();
                                    if (isEmptyString(name) || isEmptyString(pwd)) {
                                        toast("用户名或密码不能为空");
                                    }
                                    Util.saveHotspotData(name, pwd);
                                    if (getHotspotName().equals(name) && getHotspotPwd().equals
                                            (pwd)) {
                                        ssid = name;
                                        password = pwd;
                                        if (mManager == null) {
                                            mManager = new HotspotManager(MainActivity.this);
                                        }
                                        mManager.openHotspot(ssid, password);
                                        toast("热点修改成功并且已开启");
                                    } else {
                                        toast("修改失败" + ssid + password);
                                    }
                                }
                            }).setNegativeButton("取消", null)
                            .create();
                }
                mSetHotspotDialog.show();
                break;
            case R.id.hotspot_open:
                if (mManager == null) {
                    mManager = new HotspotManager(this);
                }
                if (mManager.openHotspot(ssid, password)) {
                    toast("热点开启成功");
                }
                break;
            case R.id.hotspot_close:
                if (mManager == null) {
                    mManager = new HotspotManager(this);
                }
                mManager.closeHotspot();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.conn_equipment,R.id.add_equipment})
    void click(View view) {
        switch (view.getId()) {
            case R.id.conn_equipment:
                EquipConnActivity.startActivity(this);
                break;
            case R.id.add_equipment:
                startActivity(new Intent(MainActivity.this, EquipmentActivity.class));
                break;
        }
    }
}
