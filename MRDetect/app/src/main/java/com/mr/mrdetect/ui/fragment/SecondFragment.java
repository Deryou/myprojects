package com.mr.mrdetect.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.ViewPagerAdapter;
import com.mr.mrdetect.base.BaseMainFragment;
import com.mr.mrdetect.ui.fragment.quipment.ControlerFragment;
import com.mr.mrdetect.ui.fragment.quipment.ProductFragment;
import com.mr.mrdetect.ui.fragment.quipment.SensorFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends BaseMainFragment {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<Fragment> mFragments;
    private ViewPagerAdapter mViewPagerAdapter;

    public static SecondFragment newInstance() {
        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initEventAndData() {
        mFragments = new ArrayList<>();
        mFragments.add(ProductFragment.newInsatance());
        mFragments.add(ControlerFragment.newInstance());
        mFragments.add(SensorFragment.newInstance());
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),mFragments);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_second;
    }
}
