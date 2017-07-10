package com.mr.bst.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mr.bst.R;

public class FactorRWActivity extends BaseActivity {

    public static void startActiivty(Context context) {
        Intent intent = new Intent(context, FactorRWActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_factor_rw;
    }

    @Override
    public String setToolbarTitle() {
        return "系数读写";
    }
}
