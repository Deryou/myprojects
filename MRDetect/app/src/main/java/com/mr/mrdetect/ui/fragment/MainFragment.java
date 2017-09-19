package com.mr.mrdetect.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.mrdetect.R;
import com.mr.mrdetect.event.StartBrotherEvent;
import com.mr.mrdetect.event.TabSelectedEvent;
import com.mr.mrdetect.widget.BottomBar;
import com.mr.mrdetect.widget.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends SupportFragment {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;

    public static MainFragment newInstance(){
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(FirstFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInstance();
            mFragments[THIRD] = ThirdFragment.newInstance();
            mFragments[FOURTH] = FourthFragment.newInstance();

            loadMultipleRootFragment(R.id.tab_container, FIRST, mFragments[FIRST],
                    mFragments[SECOND], mFragments[THIRD], mFragments[FOURTH]);
        } else {
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(SecondFragment.class);
            mFragments[THIRD] = findFragment(ThirdFragment.class);
            mFragments[FOURTH] = findFragment(FourthFragment.class);
        }
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        mBottomBar = view.findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(_mActivity, R.mipmap.ic_home_white_24dp, getString(R
                .string.tab_home)))
                .addItem(new BottomBarTab(_mActivity,R.mipmap.ic_message_white_24dp,getString(R.string.tab_message)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_discover_white_24dp, getString(R.string
                        .tab_discover)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_account_circle_white_24dp, getString(R.string
                        .tab_account)));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position],mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * start other BrotherFragment
     * @param event
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
