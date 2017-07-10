package com.mr.detector.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.mr.detector.callback.BackPressedHandler;

import java.util.List;

/**
 * Created by Fishy on 2016/8/5.
 * Fragment使用的返回键监听工具类，模仿系统View层的事件分发原则
 * 通过handleFragmentBack方法，传入是Activity还是Fragment，以及指定模式，来判断是否需要监听事件
 * 需要传递的Fragment实现BackPressedHandler接口即可
 */


public class BackPressedUtil {
    public static int MODE_USE_VIEWPAGER = 0x00; // 使用ViewPager来管理Fragment
    public static int MODE_NOTUSE_VIEWPAGER = 0x01; // 不使用ViewPager来管理Fragment

    /**
     * 判断是否需要消费返回键事件
     *
     * @param fragmentManager Activity和父Fragment应使用不同的FragmentManager
     * @param mode            模式是否为使用ViewPager
     * @return 是否处理
     */
    public static boolean handleFragmentBackEvent(
            FragmentManager fragmentManager, int mode) {
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (isFragmentHandlerBack(fragment, mode)) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Activity处理的入口
     *
     * @param activity activity分发给它的fragment
     * @param mode     模式是否为使用ViewPager
     * @return 是否处理
     */
    public static boolean handleFragmentBack(FragmentActivity activity, int mode) {
        return handleFragmentBackEvent(activity.getSupportFragmentManager(),
                mode);
    }

    /**
     * 向下分发的父Fragment的入口
     *
     * @param fragment fragment分发给它的子fragment
     * @return 是否处理
     */
    public static boolean handleFragmentBack(Fragment fragment, int mode) {
        return handleFragmentBackEvent(fragment.getChildFragmentManager(), mode);
    }

    /**
     * 只执行当前处于交互顶层的Fragment的返回键事件
     *
     * @param fragment
     * @param mode     模式是否为使用ViewPager
     * @return 最底层的Fragment是否消费该返回键事件
     */
    public static boolean isFragmentHandlerBack(Fragment fragment, int mode) {
        if (mode == MODE_NOTUSE_VIEWPAGER) {
            if (fragment.isVisible() && fragment instanceof BackPressedHandler) {
                return ((BackPressedHandler) fragment).onBackPressed();
            }
        } else if (mode == MODE_USE_VIEWPAGER) {
            if (fragment.getUserVisibleHint()
                    && fragment instanceof BackPressedHandler) {
                return ((BackPressedHandler) fragment).onBackPressed();
            }
        }
        return false;
    }
}
