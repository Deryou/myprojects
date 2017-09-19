package com.mr.mrdetect.ui.fragment.quipment;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.SensorAdapter;
import com.mr.mrdetect.base.RootFragment;
import com.mr.mrdetect.mvp.constract.SensorContract;
import com.mr.mrdetect.mvp.module.bean.SensorBean;
import com.mr.mrdetect.mvp.presenter.SensorPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SensorFragment extends RootFragment implements SensorContract.View {

    @BindView(R.id.view_main)
    RecyclerView mViewMain;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private List<SensorBean.SensorData> mSensorDatas;
    private SensorAdapter mSensorAdapter;
    private SensorContract.Presenter mPresenter;

    public static SensorFragment newInstance() {
        SensorFragment fragment = new SensorFragment();
        return fragment;
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_sensor;
    }

    @Override
    public void initEventAndData() {
        super.initEventAndData();
        mPresenter = new SensorPresenter(mContext,this);
        mSensorDatas = new ArrayList<>();
        mSensorAdapter = new SensorAdapter(mSensorDatas);
        mViewMain.setLayoutManager(new LinearLayoutManager(mContext));
        mViewMain.setAdapter(mSensorAdapter);
        mPresenter.getSensorData();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSensorData();
            }
        });
    }

    @Override
    public void stateError() {
        super.stateError();
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showContent(SensorBean bean) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mSensorDatas.clear();
        if (bean.getSensorDatas().size() < 1) {
            //无更多数据处理
        }
        mSensorDatas.addAll(bean.getSensorDatas());
        mSensorAdapter.notifyDataSetChanged();
    }
}
