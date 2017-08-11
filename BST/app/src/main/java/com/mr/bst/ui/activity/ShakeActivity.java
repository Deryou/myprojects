package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.mr.bst.R;
import com.mr.bst.util.TcpClient;
import com.mr.bst.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import butterknife.OnClick;

public class ShakeActivity extends BaseChartActivity {

    @BindView(R.id.now_shake)
    EditText mNowShake;
    @BindView(R.id.custom_title)
    TextView mCustomTitle;
    @BindView(R.id.start_shake)
    EditText mStartShake;
    @BindView(R.id.get_start_shake)
    Button mGetStartShake;
    @BindView(R.id.after_shake)
    EditText mAfterShake;
    @BindView(R.id.get_after_shake)
    Button mGetAfterShake;
    @BindView(R.id.shake_ave)
    EditText mShakeAve;
    @BindView(R.id.save_btn)
    CircularProgressButton mSaveBtn;

    private float startShake = 0;
    private float afterShake = 0;
    private float shake_ave = 0;

    private float shakeData[] = new float[1];
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                TastyToast.makeText(getApplicationContext(), "温压数据请求成功", TastyToast
                        .LENGTH_LONG, TastyToast.SUCCESS);
            } else if (msg.what == 1) {
                setChartData(shakeData);
                mNowShake.setText(shakeData + "");
            }
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShakeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shake;
    }

    @Override
    public String setToolbarTitle() {
        return "振动测试";
    }

    @Override
    public String setDataName(int type) {
        return "振动";
    }

    @Override
    protected void sendRequestData(String tpNum) {
        if (mTcpServer.write(Util.getShakeRequestData(tpNum))) {
            mTcpServer.openConn(true);
        }
    }

    @Override
    public void OnDataReceived(TcpClient client, byte[] data) {
        msg = Message.obtain();
        String sendData = new String(data);
        if (sendData.contains("++")) {
            String subData = sendData.split("\\+\\+")[0];
            if (sendData.contains("OK")) {
                if (sendData.substring(sendData.indexOf("end") + 3, sendData.indexOf("OK")).trim()
                        .equals("ZA")) {
                    msg.what = 0;
                }
            }
        } else if (sendData.trim().endsWith("end")) {
            if (sendData.contains("Da")) {
                shakeData = Util.getShakeData(sendData);
                msg.what = 1;
            }
        } else {
            msg.what = 2;
        }
        mHandler.sendMessage(msg);
    }

    public void clearData() {
        mLineChart.clearValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTcpServer.openConn(false);
    }

    @OnClick({R.id.get_start_shake, R.id.get_after_shake, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_start_shake:
                startShake = shakeData[0];
                mStartShake.setText(startShake + "");
                break;
            case R.id.get_after_shake:
                afterShake = shakeData[0];
                mAfterShake.setText(afterShake + "");
                break;
            case R.id.save_btn:
                shake_ave =Math.round((afterShake + startShake)/2 * 10) / 10f;
                mShakeAve.setText(shake_ave + "");
                break;
        }
    }
}
