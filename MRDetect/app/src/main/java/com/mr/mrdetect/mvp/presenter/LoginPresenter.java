package com.mr.mrdetect.mvp.presenter;

import android.content.Context;

import com.mr.mrdetect.callback.ResponseCallback;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.LoginContract;
import com.mr.mrdetect.mvp.module.bean.ResponseBean;
import com.mr.mrdetect.utils.DBUtil;
import com.mr.mrdetect.utils.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by MR on 2017/8/29.
 */

public class LoginPresenter  implements LoginContract
        .Presenter {

    private Context mContext;
    private Map<String, String> parmCode = new HashMap<>();
    private LoginContract.View mView;

    public LoginPresenter(Context context,LoginContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void loginDeal(final String username, String password) {
        if (!NetworkUtil.isNetConnected(mContext)) {
            mView.showMsg("当前网络无连接");
            return;
        }
        String url = Url.BASE_URL + Url.LOGIN;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(new ResponseCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResponseBean response, int id) {
                        if (response.getCode() == Url.RESPONSE_OK) {
                            JSONTokener jsonTokener = new JSONTokener(response.getBody());
                            try {
                                JSONObject jsonObject = new JSONObject(jsonTokener);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("message");
                                if (code == 200) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String userid = data.getString("userid");
                                    String usergroup = data.getString("groupid");
                                    DBUtil.saveUserInfo(username, userid, usergroup);
                                    mView.loginSuccess();
                                }
                                if (code == 203) {
                                    mView.showMsg(msg);
                                }
                                if (code == 204) {
                                    mView.showMsg(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
