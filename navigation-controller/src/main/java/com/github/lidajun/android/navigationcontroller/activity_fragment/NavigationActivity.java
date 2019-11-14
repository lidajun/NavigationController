package com.github.lidajun.android.navigationcontroller.activity_fragment;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.github.lidajun.android.navigationcontroller.R;

import java.util.ArrayList;

/**
 * 导航：
 * 作为滑动导航fragment的activity使用
 * Created by lidajun on 17-4-21.
 */

public abstract class NavigationActivity extends NavigationBaseActivity {

    ArrayList<NavigationFragment> mFragments = new ArrayList<>();

    void addNavigationFragment(NavigationFragment fragment) {
        mFragments.add(fragment);
        registerNavigationTouchListener(fragment);
    }

    void removeNavigationView(NavigationFragment fragment) {
        mFragments.remove(fragment);
        unRegisterNavigationTouchListener(fragment);
    }

    public void addAndCommitFragment(@IdRes int resId, @NonNull Fragment fragment) {
        addAndCommitFragment(resId, fragment, null);
    }

    public void addAndCommitFragment(@IdRes int resId, @NonNull Fragment fragment, @Nullable String tag) {
        if (mFragments.size() == 0) {
            getAddFragmentTransaction(resId, fragment, tag).commit();
        } else {
            getHideAndSetAnimationsTransaction(resId, fragment, tag).commit();
        }
    }

    @SuppressLint("CommitTransaction")
    private FragmentTransaction getHideAndSetAnimationsTransaction(@IdRes int resId, @NonNull Fragment fragment, @Nullable String tag) {
        if (hasOffset) {
            return getSupportFragmentManager().beginTransaction().hide(mFragments.get(mFragments.size() - 1)).add(resId, fragment, tag).addToBackStack(null).setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit);
        } else {
            return getSupportFragmentManager().beginTransaction().hide(mFragments.get(mFragments.size() - 1)).add(resId, fragment, tag).addToBackStack(null).setCustomAnimations(R.animator.fragment_slide_left_enter, 0);
        }
    }

    public void addAndCommitAllowingStateLossFragment(@IdRes int resId, @NonNull Fragment fragment) {
        addAndCommitAllowingStateLossFragment(resId, fragment, null);
    }

    public void addAndCommitAllowingStateLossFragment(@IdRes int resId, @NonNull Fragment fragment, @Nullable String tag) {
        if (mFragments.size() == 0) {
            getAddFragmentTransaction(resId, fragment, tag).commitAllowingStateLoss();
        } else {
            getHideAndSetAnimationsTransaction(resId, fragment, tag).commitAllowingStateLoss();
        }
    }

    @SuppressLint("CommitTransaction")
    private FragmentTransaction getAddFragmentTransaction(@IdRes int resId, @NonNull Fragment fragment, @Nullable String tag) {
        return getSupportFragmentManager().beginTransaction().add(resId, fragment, tag).addToBackStack(null);
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
    @Override
    String getNavigationText() {
        if (mFragments.size() > 1) {
            return mFragments.get(mFragments.size() - 2).toolbarTitle;
        } else {
            return "";
        }
    }

    /**
     * 下一下导航文字
     */
    @Override
    String getNavigationBackText() {
        if (mFragments.size() > 2) {
            return mFragments.get(mFragments.size() - 3).toolbarTitle;
        } else {
            return "";
        }
    }

    /**
     * 切换fragment时做一个动画
     */
    @Override
    void popBackStack() {
        if (mFragments.size() < 2) {
            return;
        }
        final View currentView = mFragments.get(mFragments.size() - 1).getView();
        final View popBackView = mFragments.get(mFragments.size() - 2).getView();
        backStack(currentView, popBackView);
    }

}
