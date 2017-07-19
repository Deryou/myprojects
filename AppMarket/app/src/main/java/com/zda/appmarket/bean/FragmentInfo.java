package com.zda.appmarket.bean;

/**
 * Created by MR on 2017/7/18.
 */

public class FragmentInfo {
    private String title;
    private Class fragment;

    public FragmentInfo(String title, Class fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public FragmentInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public Class getFragment() {
        return fragment;
    }

    public FragmentInfo setFragment(Class fragment) {
        this.fragment = fragment;
        return this;
    }
}
