package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.mr.bst.R;

import butterknife.BindView;
import butterknife.OnClick;

public class TPSaveDataActivity extends BaseActivity {

    @BindView(R.id.diff_press)
    EditText mDiffPress;
    @BindView(R.id.press_ave)
    EditText mPressAve;
    @BindView(R.id.start_press)
    EditText mStartPress;
    @BindView(R.id.after_temp)
    EditText mAfterTemp;
    @BindView(R.id.raised_temp)
    EditText mRaisedTemp;
    @BindView(R.id.save_data)
    Button mSaveData;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, TPSaveDataActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tpsave_data;
    }

    @Override
    public String setToolbarTitle() {
        return "温压数据保存";
    }

    @OnClick(R.id.save_data)
    public void onViewClicked() {

    }
}
