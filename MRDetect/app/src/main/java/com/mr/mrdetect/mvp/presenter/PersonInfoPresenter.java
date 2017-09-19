package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.PersonInfoContract;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.mvp.module.bean.UserInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/4.
 */
public class PersonInfoPresenter implements
        PersonInfoContract
        .Presenter {

    private Context mContext;
    private PersonInfoContract.View mView;

    public PersonInfoPresenter(Context context,PersonInfoContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void getUserData() {
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.PERSON_INFO)
//                .addParams("userid", DBUtil.getUserId())
                .addParams("userid", "1")
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.showMsg("获取失败");
                        mView.stateError();
                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        UserInfo userInfo = new Gson().fromJson(response.getBody(), UserInfo.class);
                        mView.loadSuccess(userInfo.getData());
                    }
                });
    }

    @Override
    public void modifyData() {

    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
