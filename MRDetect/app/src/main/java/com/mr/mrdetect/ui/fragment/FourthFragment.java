package com.mr.mrdetect.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.ReportListAdapter;
import com.mr.mrdetect.base.BaseMainFragment;
import com.mr.mrdetect.base.RootFragment;
import com.mr.mrdetect.mvp.constract.ReportContract;
import com.mr.mrdetect.mvp.module.bean.ReportBean;
import com.mr.mrdetect.mvp.presenter.ReportPresenter;
import com.mr.mrdetect.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourthFragment extends BaseMainFragment implements ReportContract.View {


    @BindView(R.id.report_list)
    RecyclerView mReportList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private List<ReportBean.ReportData> mReportDatas;
    private ReportListAdapter mReportListAdapter;
    private ReportContract.Presenter mPresenter;

    public static FourthFragment newInstance() {
        Bundle args = new Bundle();
        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_fourth;
    }

    @Override
    public void initEventAndData() {
        super.initEventAndData();
        mPresenter = new ReportPresenter(mContext,this);
        mReportDatas = new ArrayList<>();
        mReportListAdapter = new ReportListAdapter(mReportDatas);
        mReportList.setLayoutManager(new LinearLayoutManager(mContext));
        mReportList.setAdapter(mReportListAdapter);
        mPresenter.getReportData();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getReportData();
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
        SnackbarUtil.showLong(getActivity().findViewById(android.R.id.content), msg);
    }

    @Override
    public void showContent(ReportBean data) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mReportDatas.clear();
        mReportDatas.addAll(data.getReportDatas());
        mReportListAdapter.notifyDataSetChanged();
    }
}
