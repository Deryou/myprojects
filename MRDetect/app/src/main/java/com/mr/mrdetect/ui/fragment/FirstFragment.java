package com.mr.mrdetect.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.UltraPagerAdapter;
import com.mr.mrdetect.base.BaseMainFragment;
import com.mr.mrdetect.gloable.Url;
import com.mr.mrdetect.mvp.constract.HomeContract;
import com.mr.mrdetect.mvp.module.bean.BannerBean;
import com.mr.mrdetect.mvp.presenter.HomePresenter;
import com.mr.mrdetect.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends BaseMainFragment implements HomeContract.View, OnBannerListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.home_banner)
    Banner mBanner;
    private UltraPagerAdapter adapter;
    private HomeContract.Presenter mPresenter;
    private List<String> mBannerImgs;
    private List<String> mBannerUrls;
    private List<String> mBannerTitles;

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_first;
    }

    @Override
    public void initView(View view) {
        mBannerImgs = new ArrayList<>();
        mBannerUrls = new ArrayList<>();
        mBannerTitles = new ArrayList<>();

        //初始化Presenter
        mPresenter = new HomePresenter(mContext, this);
        mPresenter.getBannerData();

        mBanner.setImages(mBannerImgs)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getBannerData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateError() {

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showBanner(List<BannerBean.BannerData> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mBannerTitles.clear();
        mBannerImgs.clear();
        mBannerUrls.clear();
        for (int i = 0; i < data.size(); i++) {
            mBannerImgs.add(Url.BASE_URL + data.get(i).imgPath);
            mBannerTitles.add(data.get(i).title);
        }

        mBanner.setImages(mBannerImgs)
                .setBannerTitles(mBannerTitles)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {

    }
}
