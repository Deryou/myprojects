package com.zda.bmt.ui.actiivty;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.zda.bmt.R;
import com.zda.bmt.callback.ServerConnCallback;
import com.zda.bmt.gloable.AppConstant;
import com.zda.bmt.ui.fragment.EquipmentFragment;
import com.zda.bmt.util.TcpClient;
import com.zda.bmt.util.TcpServer;
import com.zda.bmt.util.Util;
import com.zda.bmt.wifitools.HotspotManager;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zda.bmt.util.Util.getHotspotName;
import static com.zda.bmt.util.Util.getHotspotPwd;

public class MainActivity extends BaseActivity implements ServerConnCallback {

    @BindView(R.id.custom_title)
    TextView mCustomTitle;
    @BindView(R.id.equip_setting)
    Button mEquipSetting;
    @BindView(R.id.equipment)
    Button mEquipment;

    private HotspotManager mHotspotManager;
    private AlertDialog mSetHotspotDialog;
    private LinearLayout mHotspotSetting;
    private EditText mHotspotName;
    private EditText mHotspotPwd;
    private TextView mHotspotTitle;
    private Button dialogConfirm, dialogCancel;
    //热点名称与密码
    private String ssid, passwd;
    private TcpServer mTcpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setDialog();
    }

    private void setDialog() {
        mHotspotSetting = (LinearLayout) getLayoutInflater().inflate(R.layout.hotspot_setting,
                null);
        mHotspotTitle = (TextView) mHotspotSetting.findViewById(R.id.dialog_title);
        mHotspotName = (EditText) mHotspotSetting.findViewById(R.id.hotspot_name);
        mHotspotPwd = (EditText) mHotspotSetting.findViewById(R.id.hotspot_pwd);
        dialogConfirm = (Button) mHotspotSetting.findViewById(R.id.dialog_confirm);
        dialogCancel = (Button) mHotspotSetting.findViewById(R.id.dialog_cancel);
    }

    private void initData() {
        mHotspotManager = new HotspotManager(this);
        ssid = getHotspotName();
        if (isEmptyString(ssid)) {
            ssid = "MR-BMT";
        }
        passwd = getHotspotPwd();
        if (isEmptyString(passwd)) {
            passwd = "123456789";
        }
        Util.saveHotspotData(ssid, passwd);

        mTcpServer = TcpServer.getInstance(AppConstant.HOTSPOT_PORT);
        mTcpServer.setServerConnCallback(this);
        mTcpServer.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String setToolbarTitle() {
        return "BMT";
    }

    @Override
    public void ClientConnected(TcpClient client) {

    }

    @OnClick({R.id.equip_setting, R.id.equipment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.equip_setting:
                EquipSetActivity.startActivity(this);
                break;
            case R.id.equipment:
                EquipmentActivity.startActivity(this);
                break;
        }
    }
}
