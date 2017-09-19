package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.ReportContract;
import com.mr.mrdetect.mvp.module.bean.ReportBean;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.utils.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by MR on 2017/9/7.
 */

public class ReportPresenter  implements ReportContract
        .Presenter{
    private Context mContext;
    private String start = "0";
    private ReportContract.View mView;

    public ReportPresenter(Context context,ReportContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void getReportData() {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            mView.stateError();
            return;
        }
        mView.stateLoading();
        OkHttpUtils
                .post()
                .url(Url.REPORT_LIST)
                .addParams("userid", "1")
                .addParams("start", start)
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.stateError();
                        mView.showMsg("内容获取失败");
                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        ReportBean bean = new Gson().fromJson(response.getBody(), ReportBean.class);
//                        if (bean.getReportDatas().size() > 0) {
//                            start = bean.getReportDatas().get(bean.getReportDatas().size() - 1).id;
//                        }
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
