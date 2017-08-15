package com.lurenshuo.android.navigationcontroller.activity_fragment;

import android.view.View;

import java.util.ArrayList;

/**
 * 导航：
 * 作为滑动导航fragment的activity使用
 * Created by lidajun on 17-4-21.
 */

public abstract class NavigationActivityV4 extends NavigationBaseActivity {

    ArrayList<NavigationFragmentV4> mFragments = new ArrayList<>();

    void addNavigationFragment(NavigationFragmentV4 fragment) {
        mFragments.add(fragment);
        registerNavigationTouchListener(fragment);
    }

    void removeNavigationView(NavigationFragmentV4 fragment) {
        mFragments.remove(fragment);
        unRegisterNavigationTouchListener(fragment);
    }

    /**
     * 导航的view
     */
    @Override
    View getNavigationView() {
        if (mFragments.size() > 1) {
            return mFragments.get(mFragments.size() - 2).getView();
        } else {
            return null;
        }
    }

    /**
     * 当前的view
     */
    @Override
    View getCurrentView() {
        if (mFragments.size() >= 1) {
            return mFragments.get(mFragments.size() - 1).getView();
        } else {
            return null;
        }
    }

    /**
     * 导航文字
     */
    String getNavigationText() {
        if (mFragments.size() > 1) {
            return mFragments.get(mFragments.size() - 2).mAnimatorTitle;
        } else {
            return "";
        }
    }

    /**
     * 下一下导航文字
     */
    String getNextNavigationText() {
        if (mFragments.size() > 2) {
            return mFragments.get(mFragments.size() - 3).mAnimatorTitle;
        } else {
            return "";
        }
    }

    /**
     * 切换fragment时做一个动画
     */
    protected void popBackStack() {
        final View currentView = mFragments.get(mFragments.size() - 1).getView();
        final View popBackView = mFragments.get(mFragments.size() - 2).getView();
        backStack(currentView, popBackView);
    }

}
