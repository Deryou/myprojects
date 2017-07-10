package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.mr.bst.R;
import com.mr.bst.util.TcpClient;

import butterknife.BindView;
import butterknife.OnClick;

public class EquipCalibrateActivity extends BaseChartActivity {

    @BindView(R.id.factor_r_w)
    Button mFactorRW;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EquipCalibrateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String setToolbarTitle() {
        return "设备校准";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_equip_calibrate;
    }

    @OnClick({R.id.factor_r_w})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.factor_r_w:
                FactorRWActivity.startActiivty(this);
                break;
        }
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {

    }

    @Override
    protected void sendRequestData(String equipNum) {

    }
}
