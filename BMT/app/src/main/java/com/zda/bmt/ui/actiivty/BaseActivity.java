package com.zda.bmt.ui.actiivty;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.zda.bmt.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ButterKnife.bind(this);
        initToolbar();
    }

    public abstract int getLayoutId();

    private void initToolbar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager
                    .LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (mToolbar != null) {
            mToolbar.setTitle(setToolbarTitle());
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 设置toolbar标题
     * @return
     */
    public abstract String setToolbarTitle();

    /**
     * 判断字符串是否为空
     * @param text
     * @return
     */
    protected boolean isEmptyString(String text) {
        return TextUtils.isEmpty(text) || text.equals("null");
    }
}
