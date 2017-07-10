package com.mr.detector.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mr.detector.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.custom_title)
    TextView mCustomTitle;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(getLayoutId());
        mContext = this;
        ButterKnife.bind(this);
        initToolbar();
    }

    public void initStyle() {

    }

    public Context getContext() {
        return mContext;
    }

    public void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (mToolbar != null) {
            mToolbar.setTitle(setToolbarTitle());
            setSupportActionBar(mToolbar);
        }
    }

    protected abstract String setToolbarTitle();

    protected abstract int getLayoutId();

    /**
     * 弹出Toast提示
     * @param text 提示内容
     */
    protected void toast(String text) {
        if(this.isFinishing()) {
            return;
        }

        if(!TextUtils.isEmpty(text)) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 判断字符串是否不为空
     * @param text
     * @return
     */
    protected boolean isNotEmptyString(String text) {
        return !TextUtils.isEmpty(text) && !text.equals("null");
    }

    /**
     * 判断字符串是否为空
     * @param text
     * @return
     */
    protected boolean isEmptyString(String text) {
        return TextUtils.isEmpty(text) || text.equals("null");
    }
}
