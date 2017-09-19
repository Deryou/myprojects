package com.mr.mrdetect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mr.mrdetect.R;
import com.mr.mrdetect.base.RootActivity;
import com.mr.mrdetect.mvp.constract.PersonInfoContract;
import com.mr.mrdetect.mvp.module.bean.UserInfo;
import com.mr.mrdetect.mvp.presenter.PersonInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoActivity extends RootActivity implements
        PersonInfoContract
        .View{

    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_name_item)
    RelativeLayout mUserNameItem;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.email_item)
    RelativeLayout mEmailItem;
    @BindView(R.id.mobile)
    TextView mMobile;
    @BindView(R.id.mobile_item)
    RelativeLayout mMobileItem;
    @BindView(R.id.real_name)
    TextView mRealName;
    @BindView(R.id.real_name_item)
    RelativeLayout mRealNameItem;

    private PersonInfoContract.Presenter mPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_person_info;
    }

    @Override
    public void initData() {
        mPresenter = new PersonInfoPresenter(this,this);
        mPresenter.getUserData();
    }

    @OnClick({R.id.user_name_item, R.id.email_item, R.id.mobile_item, R.id.real_name_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_name_item:
                break;
            case R.id.email_item:
                break;
            case R.id.mobile_item:
                break;
            case R.id.real_name_item:
                break;
        }
    }

    @Override
    public void loadSuccess(UserInfo.UserData data) {
        if (data == null) {
            mUserName.setText("");
            mEmail.setText("");
            mMobile.setText("");
            mRealName.setText("");
        } else {
            mUserName.setText(data.getUsername());
            mEmail.setText(data.getEmail());
            mMobile.setText(data.getMobile());
            mRealName.setText(data.getRealname());
        }
    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateError() {

    }

    @Override
    public void showMsg(String msg) {

    }
}
