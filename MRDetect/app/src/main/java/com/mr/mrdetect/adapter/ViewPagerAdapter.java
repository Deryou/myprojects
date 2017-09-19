package com.mr.mrdetect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by MR on 2017/9/5.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragments;
    private String[] titles = {"产品分类", "控制器信息", "传感器信息"};

    public ViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
