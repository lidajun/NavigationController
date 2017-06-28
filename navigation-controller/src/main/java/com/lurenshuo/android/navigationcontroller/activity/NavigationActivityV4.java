package com.lurenshuo.android.navigationcontroller.activity;

import android.view.View;

import com.lurenshuo.android.navigationcontroller.fragment.NavigationFragmentV4;

import java.util.ArrayList;

/**
 * 导航：
 * 作为滑动导航fragment的activity使用
 * Created by lidajun on 17-4-21.
 */

public abstract class NavigationActivityV4 extends NavigationBaseActivity {

    public ArrayList<NavigationFragmentV4> mFragments = new ArrayList<>();

    public void addNavigationFragment(NavigationFragmentV4 fragment) {
        mFragments.add(fragment);
        registerNavigationTouchListener(fragment);
    }

    public void removeNavigationView(NavigationFragmentV4 fragment) {
        mFragments.remove(fragment);
        unRegisterNavigationTouchListener(fragment);
    }

    /**
     * 导航的view
     */
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
    public void popBackStack() {
        final View currentView = mFragments.get(mFragments.size() - 1).getView();
        final View popBackView = mFragments.get(mFragments.size() - 2).getView();
        backStack(currentView, popBackView);
    }

}
