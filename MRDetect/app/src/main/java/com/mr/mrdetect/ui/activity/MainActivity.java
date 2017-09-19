package com.mr.mrdetect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mr.mrdetect.R;
import com.mr.mrdetect.base.BaseMainFragment;
import com.mr.mrdetect.mvp.constract.CompanyInfoContract;
import com.mr.mrdetect.ui.fragment.MainFragment;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends BaseActivity{

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.draw_layout)
    DrawerLayout mDrawerLayout;

    private View headerView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.main_container,MainFragment.newInstance());
        }
        headerView = mNavigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "header", Toast.LENGTH_SHORT).show();
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_user_info:
                        PersonInfoActivity.startActivity(MainActivity.this);
                        break;
                    case R.id.menu_company_info:
                        CompanyInfoActivity.startActivity(MainActivity.this);
                        break;
                }
                return false;
            }
        });
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public String setToolbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressedSupport() {
        //对于4个类别的Fragment内的回退back逻辑，已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
