package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.AddControlerContract;
import com.mr.mrdetect.mvp.module.bean.AddProductBean;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.utils.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/16.
 */

public class AddControlerPresenter implements AddControlerContract.Presenter {

    private AddControlerContract.View mView;
    private Context mContext;

    public AddControlerPresenter(Context context, AddControlerContract.View view) {
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
    public void getProductType() {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            mView.stateError();
            return;
        }
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.PRODUCT_SELECT_LIST)
                .addParams("userid", "1")
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {

                    }
                });
    }

    @Override
    public void addProduct(String categoryId, String equipDesc, String equipGUID, boolean isValue) {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            mView.stateError();
            return;
        }
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.ADD_CONTROLER)
                .addParams("userid", "1")
                .addParams("category_id", categoryId)
                .addParams("description", equipDesc)
                .addParams("identification", equipGUID)
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        AddProductBean bean = new Gson().fromJson(response.getBody(),
                                AddProductBean.class);
                        mView.showMsg(bean.getMessage());
                        mView.addSuccess();
                    }
                });
    }
}
