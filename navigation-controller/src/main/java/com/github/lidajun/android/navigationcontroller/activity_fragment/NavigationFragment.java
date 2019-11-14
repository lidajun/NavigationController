package com.github.lidajun.android.navigationcontroller.activity_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.github.lidajun.android.navigationcontroller.listener.NavigationTouchListener;
import com.github.lidajun.android.navigationcontroller.listener.PopBackListener;

/**
 * 导航：
 * 作为滑动导航的父fragment使用
 * 1,添加fragment时，需要 fragmentTransaction.addToBackStack(null);
 * 2,不能使用 fragmentTransaction.setTransition(XXX) ,否则没有导航view的动画
 * 3,添加切换动画时使用两个参数的，这两个参数的是只做开始的动画，popBackTack的动画由NavigationActivity做
 * 例如：transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,R.animator.fragment_slide_left_exit);
 * Created by lidajun on 17-4-21.
 */

public abstract class NavigationFragment extends Fragment implements NavigationTouchListener {
    NavigationActivity mActivity;
    String toolbarTitle;

    /**
     * 注册touch事件
     * 添加view
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (NavigationActivity) activity;
        mActivity.addNavigationFragment(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity.mFragmentHelper.created();
        if (null != mActivity.mNavigationToolbar) {
            mActivity.mNavigationToolbar.mTitleTv.setText(toolbarTitle);
        }
    }

    /**
     * 添加toolbarTitle
     */
    public void setToolbarTitle(String title) {
        toolbarTitle = title;
    }

    public void setToolbarTitle(@StringRes int resId) {
        toolbarTitle = getString(resId);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.mFragmentHelper.initToolbarNavigationText(toolbarTitle);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mActivity.mFragmentHelper.initToolbarNavigationText(toolbarTitle);
        }
    }

    /**
     * 取消注册touch事件和移除view
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.removeNavigationView(this);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return mActivity.mFragmentHelper.touchEvent(event, new PopBackListener() {
            @Override
            public void popBack() {
                FragmentManager fm = getFragmentManager();
                if (null != fm) {
                    fm.popBackStack();
                }
            }
        });
    }
}
