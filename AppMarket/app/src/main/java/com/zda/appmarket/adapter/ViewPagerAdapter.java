package com.zda.appmarket.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zda.appmarket.bean.FragmentInfo;
import com.zda.appmarket.ui.fragment.CategoryFragment;
import com.zda.appmarket.ui.fragment.GamesFragment;
import com.zda.appmarket.ui.fragment.RankingFragment;
import com.zda.appmarket.ui.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR on 2017/7/18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentInfo> mFraments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragmnets();
    }

    private void initFragmnets() {
        mFraments.add(new FragmentInfo("推荐", RecommendFragment.class));
        mFraments.add(new FragmentInfo("排行", RankingFragment.class));
        mFraments.add(new FragmentInfo("游戏", GamesFragment.class));
        mFraments.add(new FragmentInfo("分类", CategoryFragment.class));
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return (Fragment) mFraments.get(position).getFragment().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFraments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFraments.get(position).getTitle();
    }
}
