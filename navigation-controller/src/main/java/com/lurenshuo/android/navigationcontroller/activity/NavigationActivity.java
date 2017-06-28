package com.lurenshuo.android.navigationcontroller.activity;

import android.view.View;

import com.lurenshuo.android.navigationcontroller.fragment.NavigationFragment;

import java.util.ArrayList;

/**
 * 导航：
 * 作为滑动导航fragment的activity使用
 * Created by lidajun on 17-4-21.
 */

public abstract class NavigationActivity extends NavigationBaseActivity {

    public ArrayList<NavigationFragment> mFragments = new ArrayList<>();

    public void addNavigationFragment(NavigationFragment fragment) {
        mFragments.add(fragment);
        registerNavigationTouchListener(fragment);
    }

    public void removeNavigationView(NavigationFragment fragment) {
        mFragments.remove(fragment);
        unRegisterNavigationTouchListener(fragment);
    }

    /**
     * 导航的view
     */
    @Override
    public View getNavigationView() {
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
    public View getCurrentView() {
        if (mFragments.size() >= 1) {
            return mFragments.get(mFragments.size() - 1).getView();
        } else {
            return null;
        }
    }

    /**
     * 导航文字
     */
    @Override
    public String getNavigationText() {
        if (mFragments.size() > 1) {
            return mFragments.get(mFragments.size() - 2).mAnimatorTitle;
        } else {
            return "";
        }
    }

    /**
     * 下一下导航文字
     */
    @Override
    public String getNextNavigationText() {
        if (mFragments.size() > 2) {
            return mFragments.get(mFragments.size() - 3).mAnimatorTitle;
        } else {
            return "";
        }
    }

    /**
     * 切换fragment时做一个动画
     */
    @Override
    public void popBackStack() {
        final View currentView = mFragments.get(mFragments.size() - 1).getView();
        final View popBackView = mFragments.get(mFragments.size() - 2).getView();
        backStack(currentView, popBackView);
    }

}
