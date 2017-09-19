package com.mr.mrdetect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.constract.CompanyInfoContract;
import com.mr.mrdetect.mvp.module.bean.CompanyInfoBean;
import com.mr.mrdetect.mvp.presenter.CompanyInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyInfoActivity extends BaseActivity implements CompanyInfoContract.View {

    @BindView(R.id.company_name)
    TextView mCompanyName;
    @BindView(R.id.company_name_item)
    RelativeLayout mCompanyNameItem;
    @BindView(R.id.company_desc)
    TextView mCompanyDesc;
    @BindView(R.id.company_desc_item)
    LinearLayout mCompanyDescItem;
    @BindView(R.id.company_address)
    TextView mCompanyAddress;
    @BindView(R.id.company_address_item)
    RelativeLayout mCompanyAddressItem;
    @BindView(R.id.company_email)
    TextView mCompanyEmail;
    @BindView(R.id.company_email_item)
    RelativeLayout mCompanyEmailItem;
    @BindView(R.id.company_zipcode)
    TextView mCompanyZipcode;
    @BindView(R.id.company_zipcode_item)
    RelativeLayout mCompanyZipcodeItem;
    @BindView(R.id.company_phone)
    TextView mCompanyPhone;
    @BindView(R.id.company_phone_item)
    RelativeLayout mCompanyPhoneItem;
    private CompanyInfoContract.Presenter mPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CompanyInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initData() {
        mPresenter = new CompanyInfoPresenter(this, this);
        mPresenter.getCompanyInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    public String setToolbarTitle() {
        return "公司信息列表";
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_company_info;
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

    @Override
    public void showCompanyInfo(CompanyInfoBean.CompanyData data) {
        mCompanyName.setText(data.name);
        mCompanyDesc.setText(data.desc);
        mCompanyAddress.setText(data.address);
        mCompanyEmail.setText(data.email);
        mCompanyZipcode.setText(data.zipcode);
        mCompanyPhone.setText(data.phone);
    }

    @OnClick({R.id.company_name_item, R.id.company_desc_item, R.id.company_address_item, R.id
            .company_email_item, R.id.company_zipcode_item, R.id.company_phone_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.company_name_item:
                break;
            case R.id.company_desc_item:
                break;
            case R.id.company_address_item:
                break;
            case R.id.company_email_item:
                break;
            case R.id.company_zipcode_item:
                break;
            case R.id.company_phone_item:
                break;
        }
    }
}
