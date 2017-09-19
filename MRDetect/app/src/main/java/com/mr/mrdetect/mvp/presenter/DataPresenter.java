package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.DataContract;
import com.mr.mrdetect.mvp.module.bean.DataBean;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.utils.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/8.
 */

public class DataPresenter  implements DataContract
        .Presenter{
    private static final String TAG = "DataPresenter";

    private Context mContext;
    private DataContract.View mView;

    public DataPresenter(Context context,DataContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void getData() {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            mView.stateError();
            return;
        }
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.DATA_LIST)
                .addParams("userid","1")
                .addParams("groupid","3")
                .addParams("type","temperature")
                .addParams("start_time","1")
                .addParams("start","0")
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.stateError();
                        mView.showMsg("获取内容失败");
                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        DataBean bean = new Gson().fromJson(response.getBody(), DataBean.class);
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
