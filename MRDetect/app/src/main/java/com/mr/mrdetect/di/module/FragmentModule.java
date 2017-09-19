package com.mr.mrdetect.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by MR on 2017/9/12.
 */

//@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

//    @Provides
//    @FragmentScope
    public Activity provideActivity() {
        return mFragment.getActivity();
    }
}
