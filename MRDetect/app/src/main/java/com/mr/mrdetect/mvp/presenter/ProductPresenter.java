package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.ProductContract;
import com.mr.mrdetect.mvp.module.bean.ProductBean;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.utils.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/6.
 */

public class ProductPresenter implements
        ProductContract
        .Presenter {
    private static final String TAG = "ProductPresenter";

    private Context mContext;
    private ProductContract.View mView;

    public ProductPresenter(Context context,ProductContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void getProductData() {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            mView.stateError();
            return;
        }
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.PRODUCT_TYPE_LIST)
                .addParams("userid", "1")
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.stateError();
                        mView.showMsg("获取内容失败");
                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        ProductBean bean = new Gson().fromJson(response.getBody(), ProductBean
                                .class);
                        if (bean.getCode() == 200) {
                            mView.showContent(bean);
                        }
                        if (bean.getCode() == 203) {
                            mView.stateEmpty();
                            mView.showMsg(bean.getMessage());
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
