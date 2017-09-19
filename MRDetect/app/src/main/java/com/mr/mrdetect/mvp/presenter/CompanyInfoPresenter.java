package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.CompanyInfoContract;
import com.mr.mrdetect.mvp.module.bean.CompanyInfoBean;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.utils.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/14.
 */

public class CompanyInfoPresenter implements CompanyInfoContract.Presenter{

    private CompanyInfoContract.View mView;
    private Context mContext;

    public CompanyInfoPresenter(Context context, CompanyInfoContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void getCompanyInfo() {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            mView.stateError();
            return;
        }
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.COMPANY_INFO)
                .addParams("userid","1")
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        CompanyInfoBean bean = new Gson().fromJson(response.getBody(),
                                CompanyInfoBean.class);
                        if (bean.getCode() == Url.SUCCESS_CODE) {
                            mView.showCompanyInfo(bean.getCompanyDatas());
                        } else if (bean.getCode() == 203) {
                            mView.showMsg(bean.getMessage());
                        } else {
                            mView.showMsg("未知错误");
                        }
                    }
                });
    }

    @Override
    public void modifyCompanyInfo() {

    }
}
