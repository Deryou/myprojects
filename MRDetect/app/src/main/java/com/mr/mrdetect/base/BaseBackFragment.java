package com.mr.mrdetect.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by MR on 2017/8/25.
 */

public class BaseBackFragment extends SwipeBackFragment {
    private static final String TAG = "BaseBackFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setParallaxOffset(0.5f);
    }
}
