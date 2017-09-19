package com.mr.mrdetect.ui.fragment.quipment;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mr.mrdetect.R;
import com.mr.mrdetect.adapter.ProductAdapter;
import com.mr.mrdetect.base.RootFragment;
import com.mr.mrdetect.mvp.constract.ProductContract;
import com.mr.mrdetect.mvp.module.bean.ProductBean;
import com.mr.mrdetect.mvp.presenter.ProductPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends RootFragment  implements ProductContract.View {

    @BindView(R.id.view_main)
    RecyclerView mViewMain;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private ProductAdapter mProductAdapter;
    private List<ProductBean.ProductData> mProductDatas;
    private ProductContract.Presenter mPresenter;

    public static ProductFragment newInsatance() {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public void initEventAndData() {
        super.initEventAndData();
        mPresenter = new ProductPresenter(mContext,this);
        mProductDatas = new ArrayList<>();
        mProductAdapter = new ProductAdapter(mProductDatas);
        mViewMain.setLayoutManager(new LinearLayoutManager(mContext));
        mViewMain.setAdapter(mProductAdapter);
        mPresenter.getProductData();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getProductData();
            }
        });
    }

    @Override
    public int getLayoutSource() {
        return R.layout.fragment_product;
    }

    @Override
    public void stateError() {
        super.stateError();
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showContent(ProductBean bean) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mProductDatas.clear();
        mProductDatas.addAll(bean.getDatas());
        mProductAdapter.notifyDataSetChanged();
    }
}
