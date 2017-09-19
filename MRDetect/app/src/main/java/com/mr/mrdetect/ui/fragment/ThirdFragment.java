package com.mr.mrdetect.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.DataAdapter;
import com.mr.mrdetect.base.BaseMainFragment;
import com.mr.mrdetect.base.RootFragment;
import com.mr.mrdetect.mvp.constract.DataContract;
import com.mr.mrdetect.mvp.module.bean.DataBean;
import com.mr.mrdetect.mvp.presenter.DataPresenter;
import com.mr.mrdetect.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends BaseMainFragment implements DataContract.View {

    @BindView(R.id.data_list)
    RecyclerView mDataList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private List<DataBean.Data> mDatas;
    private DataAdapter mDataAdapter;
    private DataContract.Presenter mPresenter;

    public static ThirdFragment newInstance() {
        Bundle args = new Bundle();
        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_third;
    }

    @Override
    public void initEventAndData() {
        super.initEventAndData();
        mPresenter = new DataPresenter(mContext,this);
        mDatas = new ArrayList<>();
        mDataAdapter = new DataAdapter(mDatas);
        mDataList.setLayoutManager(new LinearLayoutManager(mContext));
        mDataList.setAdapter(mDataAdapter);
        mPresenter.getData();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData();
            }
        });
    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateError() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showMsg(String msg) {
        SnackbarUtil.showLong(getActivity().findViewById(android.R.id.content),msg);
    }

    @Override
    public void showContent(DataBean dataBean) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mDatas.clear();
        mDatas.addAll(dataBean.getDatas());
        mDataAdapter.notifyDataSetChanged();
    }
}
