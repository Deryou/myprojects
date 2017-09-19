package com.mr.mrdetect.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mr.mrdetect.base.BaseRootPresenter;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.HomeContract;
import com.mr.mrdetect.mvp.module.bean.BannerBean;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/11.
 */

public class HomePresenter extends BaseRootPresenter implements HomeContract
        .Presenter {
    private static final String TAG = "HomePresenter";

    private Context mContext;
    private HomeContract.View mView;

    public HomePresenter(Context context, HomeContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void getBannerData() {
        checkNetWork();
        mView.stateLoading();
        OkHttpUtils
                .get()
                .url(Url.HOME_BANNER)
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //TODO  提示信息去掉，换为默认图片
                        mView.showMsg("轮播图加载失败");
                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        BannerBean bean = new Gson().fromJson(response.getBody(), BannerBean.class);
                        Log.e(TAG, "onResponse: " + bean.toString());
                        if (bean.getCode() == Url.SUCCESS_CODE) {
                            if (bean.getBannerDatas() != null) {
                                mView.showBanner(bean.getBannerDatas());
                            }
                        }
                        if (bean.getCode() == 203) {
                            mView.showMsg(response.getMessage());
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
