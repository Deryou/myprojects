package com.mr.mrdetect.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mr.mrdetect.R;
import com.mr.mrdetect.base.RootActivity;
import com.mr.mrdetect.mvp.constract.LoginContract;
import com.mr.mrdetect.mvp.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends RootActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.remember_password)
    CheckBox mRememberPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.forget_password)
    Button mForgetPassword;
    @BindView(R.id.register)
    Button mRegister;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initData() {
        mPresenter = new LoginPresenter(this,this);
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void loginSuccess() {
        MainActivity.startActivity(this);
        finish();
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showMsg(String msg) {

    }


    @Override
    public void clearUserInfo() {

    }

    @OnClick({R.id.login, R.id.forget_password, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                String account = mAccount.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    mAccount.setError(getString(R.string.account_empty));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError(getString(R.string.password_empty));
                    return;
                }
                mPresenter.loginDeal(account,password);
                break;
            case R.id.forget_password:
                break;
            case R.id.register:
                break;
        }
    }
}
