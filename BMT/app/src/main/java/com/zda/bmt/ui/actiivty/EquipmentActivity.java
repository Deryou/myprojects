package com.zda.bmt.ui.actiivty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.zda.bmt.R;
import com.zda.bmt.bean.BottomTabData;

import butterknife.BindView;

public class EquipmentActivity extends BaseActivity {

    @BindView(R.id.time_gap)
    EditText mTimeGap;
    @BindView(R.id.type_32)
    RadioButton mType32;
    @BindView(R.id.type_36)
    RadioButton mType36;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.top_area)
    LinearLayout mTopArea;
    @BindView(R.id.home_container)
    FrameLayout mHomeContainer;
    @BindView(R.id.bottom_tab)
    TabLayout mBottomTab;
    //所有的fragment集合
    private Fragment[] mFragments;
    //当前的fragment
    protected Fragment currentFragment = null;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EquipmentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentData();
        initView();
    }

    public void initView() {
        mBottomTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab状态
                for (int i = 0; i < mBottomTab.getTabCount(); i++) {
                    if (i == tab.getPosition()) {
                        mBottomTab.getTabAt(i).setIcon(getResources().getDrawable(BottomTabData
                                .mTabResPressed[i]));
                    } else {
                        mBottomTab.getTabAt(i).setIcon(getResources().getDrawable(BottomTabData
                                .mTabRes[i]));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_form_select)).setText(BottomTabData.mTableTitle[0]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_temp_normal)).setText(BottomTabData.mTableTitle[1]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_humdity_normal)).setText(BottomTabData.mTableTitle[2]));
        mBottomTab.addTab(mBottomTab.newTab().setIcon(getResources().getDrawable(R.mipmap
                .icon_tab_oxygen_normal)).setText(BottomTabData.mTableTitle[3]));

        mType32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    mTemperatureCallback.tempStateChanged("32");
                }
            }
        });
        mType36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    mTemperatureCallback.tempStateChanged("36");
                }
            }
        });
    }

    private void initFragmentData() {
        mFragments = BottomTabData.getFragments();
//        mEquipmentCallback = (EquipmentCallback) mFragments[AppConstants.EQUIPMENTFRAGMENT];
//        mTemperatureCallback = (TemperatureCallback) mFragments[AppConstants.TEMPERATUREFRAGMENT];
//        mBaseFragment = (BaseFragment) mFragments[AppConstants.TEMPERATUREFRAGMENT];
//        mBaseFragment.setDataResultCallback(this);
//        mHumidtyCallback = (HumidtyCallback) mFragments[AppConstants.HUMIDITYFRAGMENT];
//        mOxygenCallback = (OxygenCallback) mFragments[AppConstants.OXYGENFRAGMENT];
//        mReportData = new ReportData();
    }


    private void onTabItemSelected(int position) {

        switch (position) {
            case 0:
                currentFragment = mFragments[0];
                mTopArea.setVisibility(View.GONE);
                break;
            case 1:
                currentFragment = mFragments[1];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            case 2:
                currentFragment = mFragments[2];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            case 3:
                currentFragment = mFragments[3];
                mTopArea.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (currentFragment != null) {
            for (Fragment fragment : mFragments) {
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
            if (!currentFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(R.id.home_container,
                        currentFragment).commit();
            }
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_equipment;
    }

    @Override
    public String setToolbarTitle() {
        return "数据采集";
    }
}
