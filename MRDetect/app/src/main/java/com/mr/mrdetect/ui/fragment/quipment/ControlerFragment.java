package com.mr.mrdetect.ui.fragment.quipment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.ControlerAdapter;
import com.mr.mrdetect.base.RootFragment;
import com.mr.mrdetect.mvp.constract.ControlerContract;
import com.mr.mrdetect.mvp.module.bean.ControlerBean;
import com.mr.mrdetect.mvp.presenter.ControlerPresenter;
import com.mr.mrdetect.ui.activity.ControlerAddActivity;
import com.mr.mrdetect.utils.CircularAnimUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlerFragment extends RootFragment implements ControlerContract
        .View {


    @BindView(R.id.view_main)
    RecyclerView mViewMain;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;

    private ControlerAdapter mControlerAdapter;
    private List<ControlerBean.ControlorData> mControlorDatas;
    private ControlerContract.Presenter mPresenter;

    public static ControlerFragment newInstance() {
        ControlerFragment fragment = new ControlerFragment();
        return fragment;
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_controlor;
    }

    @Override
    public void initEventAndData() {
        super.initEventAndData();
        mPresenter = new ControlerPresenter(mContext,this);
        mControlorDatas = new ArrayList<>();
        mControlerAdapter = new ControlerAdapter(mControlorDatas);
        mViewMain.setLayoutManager(new LinearLayoutManager(mContext));
        mViewMain.setAdapter(mControlerAdapter);
        mPresenter.getControlerData();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getControlerData();
            }
        });
    }

    @Override
    public void stateLoading() {
        super.stateLoading();
    }

    @Override
    public void stateError() {
        super.stateError();
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showContent(ControlerBean bean) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mControlorDatas.clear();
        mControlorDatas.addAll(bean.getControlorDatas());
        mControlerAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab_add)
    void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(mContext, ControlerAddActivity.class);
        CircularAnimUtil.startActivity(mActivity, intent, mFabAdd, R.color.colorPrimary);
    }
}
