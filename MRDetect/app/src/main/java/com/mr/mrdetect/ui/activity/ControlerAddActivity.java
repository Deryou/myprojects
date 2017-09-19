package com.mr.mrdetect.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.mr.mrdetect.R;
import com.mr.mrdetect.mvp.constract.AddControlerContract;
import com.mr.mrdetect.mvp.presenter.AddControlerPresenter;
import com.mr.mrdetect.utils.SnackbarUtil;
import com.mr.mrdetect.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlerAddActivity extends BaseActivity implements AddControlerContract.View {

    @BindView(R.id.product_name)
    EditText mProductName;
    @BindView(R.id.equip_desc)
    EditText mEquipDesc;
    @BindView(R.id.effective)
    RadioButton mEffective;
    @BindView(R.id.no_effective)
    RadioButton mNoEffective;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.equip_guid)
    EditText mEquipGuid;
    private AddControlerContract.Presenter mPresenter;
    private boolean isEffective = true;

    @Override
    public int getResourceId() {
        return R.layout.activity_controler_add;
    }

    @Override
    public String setToolbarTitle() {
        return "添加控制器";
    }

    @Override
    public void initData() {
        mPresenter = new AddControlerPresenter(this, this);
        mPresenter.getProductType();
        mEffective.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isEffective = true;
                }
            }
        });
        mNoEffective.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isEffective = false;
                }
            }
        });
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
        SnackbarUtil.showLong(this.findViewById(android.R.id.content), msg);
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        if (Util.isEmpty(mProductName.getText().toString())) {
            mProductName.setError("产品名称不能为空");
            return;
        }
        if (Util.isEmpty(mEquipGuid.getText().toString())) {
            mEquipGuid.setError("GUID不能为空");
            return;
        }
        mPresenter.addProduct(mProductName.getText().toString(),mEquipDesc.getText().toString(),
                mEquipGuid.getText().toString(),isEffective);
    }

    @Override
    public void addSuccess() {
        finish();
    }
}
