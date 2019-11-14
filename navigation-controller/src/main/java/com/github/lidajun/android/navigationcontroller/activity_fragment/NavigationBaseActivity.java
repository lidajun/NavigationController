package com.github.lidajun.android.navigationcontroller.activity_fragment;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.lidajun.android.navigationcontroller.listener.NavigationTouchListener;
import com.github.lidajun.android.navigationcontroller.utils.DisplayUtil;
import com.github.lidajun.android.navigationcontroller.utils.ScreenUtil;
import com.github.lidajun.android.navigationcontroller.widget.NavigationToolbar;

import java.util.ArrayList;

/**
 * Created by lidajun on 17-6-22.
 */

abstract class NavigationBaseActivity extends AppCompatActivity {
    NavigationToolbar mNavigationToolbar;
    float edgeSize;
    //动画中
    public boolean inAnimator = false;
    int mScreenWidth;
    // 保存MyTouchListener接口的列表
    ArrayList<NavigationTouchListener> mListeners = new ArrayList<>();
    //边的大小，屏幕的20分之1
    int EDGE_SIZE = 20;
    //超过多少，跳转，屏幕的4分之1
    int NAVI_BOUNDED = 4;
    private int scrollMinDistance;
    //导航模式开启中
    public boolean inNavigation = false;
    //fragment帮助类
    NavigationFragmentHelper mFragmentHelper;
    //是否有偏移动画
    protected boolean hasOffset = true;
    //可触摸的边的大小，是最小触摸的倍数
    protected int edgeTimes = 2;
    //回退的前缀
    public String backPrefix = "<";
    Drawable defaultBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentHelper = new NavigationFragmentHelper(this);
        scrollMinDistance = ViewConfiguration.get(this).getScaledTouchSlop();
        initScreenSize();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mNavigationToolbar = initNavigationToolbar();
        if (null != mNavigationToolbar) {
            mNavigationToolbar.mBackTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initScreenSize();
    }

    private void initScreenSize() {
        mScreenWidth = ScreenUtil.getScreenWidth(this);
        edgeSize = Math.max(mScreenWidth / EDGE_SIZE, scrollMinDistance * edgeTimes);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    protected NavigationToolbar initNavigationToolbar() {
        return null;
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        NavigationTouchListener listener = getLastTouchListener();
        if (null != listener && mListeners.size() > 1) {
            //如果是边模式，只跟据位置判断
            if (ev.getX() < edgeSize && ev.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        NavigationTouchListener listener = getLastTouchListener();
        if (null == listener) {
            return super.onTouchEvent(event);
        }
        return listener.onTouchEvent(event);
    }

    /**
     * 按下返回键的监听，如果是messageItem页面就可以退出
     */
    @Override
    public void onBackPressed() {
        if (mListeners.size() == 1) {
            backPressed();
        } else {
            popBackStack();
        }
    }

    abstract void popBackStack();

    NavigationTouchListener getLastTouchListener() {
        if (mListeners.size() > 1) {
            return mListeners.get(mListeners.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * 注册
     * 把Activity的touch事件传递给fragment
     */
    void registerNavigationTouchListener(NavigationTouchListener listener) {
        mListeners.add(listener);
        viewChange(mListeners.size() - 1);
    }

    /**
     * 取消
     * 把Activity的touch事件传递给fragment
     */
    void unRegisterNavigationTouchListener(NavigationTouchListener listener) {
        mListeners.remove(listener);
    }

    public void setDefaultBackground(Drawable drawable){
        defaultBackground = drawable;
    }

    abstract View getNavigationView();

    abstract View getCurrentView();

    abstract String getNavigationText();

    abstract String getNavigationBackText();

    void backStack(final View currentView, final View popBackView) {
        final int PX = DisplayUtil.dip2px(this, 100);
        if (null == currentView || null == popBackView) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0, PX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value >= PX) {
                    //让栈顶的fragment出栈
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                    viewChange(mListeners.size() - 2);
                }
                popBackView.setVisibility(View.VISIBLE);
                currentView.setX(value);
            }
        });
        animator.setDuration(100);
        animator.start();

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentView.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        alphaAnimator.setDuration(99);
        alphaAnimator.start();
    }

    /**
     * 在根fragment时按返回键
     */
    protected abstract void backPressed();

    /**
     * 功换view
     * Start at page 0
     *
     * @param page 从0页开始
     */
    public void viewChange(float page) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentHelper = null;
        mListeners = null;
        mNavigationToolbar = null;
    }

}
