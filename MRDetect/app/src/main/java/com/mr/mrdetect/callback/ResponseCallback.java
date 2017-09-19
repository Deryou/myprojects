package com.mr.mrdetect.callback;

import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by MR on 2017/8/31.
 */

public abstract class ResponseCallback extends Callback<ResponseBean> {
    @Override
    public ResponseBean parseNetworkResponse(Response response, int id) throws Exception {
        ResponseBean bean = new ResponseBean();
        bean.setCode(response.code());
        bean.setMessage(response.message());
        bean.setBody(response.body().string());
        return bean;
    }
}
