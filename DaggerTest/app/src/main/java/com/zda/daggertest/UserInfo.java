package com.zda.daggertest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MR on 2017/7/10.
 */

public class UserInfo {

    public UserInfo(Context context) {
        Toast.makeText(context, "UserINfo吐丝了了了了啦啦啦了了了  练练", Toast.LENGTH_SHORT).show();
    }
    private static final String TAG = "UserInfo";
    public void theInfo() {
        Log.e(TAG, "theInfo: UserInfoINININIINNNNIIINNFO");
    }
}
