package com.lurenshuo.android.navigationcontroller.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.lurenshuo.android.navigationcontroller.fragment.NavigationFragmentHelper;
import com.lurenshuo.android.navigationcontroller.listener.NavigationTouchListener;
import com.lurenshuo.android.navigationcontroller.utils.DisplayUtil;
import com.lurenshuo.android.navigationcontroller.utils.ScreenUtil;
import com.lurenshuo.android.navigationcontroller.widget.NavigationToolbar;

import java.util.ArrayList;

/**
 * Created by lidajun on 17-6-22.
 */

public abstract class NavigationBaseActivity extends AppCompatActivity {
    protected float mDownY;
    protected float mDownX;
    public NavigationToolbar mNavigationToolbar;
    public float edgeSize = 50;
    //动画中
    public boolean inAnimator = false;
    public int mScreenWidth;
    // 保存MyTouchListener接口的列表
    public ArrayList<NavigationTouchListener> mListeners = new ArrayList<>();
    public Mode mMode = Mode.edge;
    protected GestureDetector mGestureDetector;
    //边的大小，屏幕的20分之1
    public int EDGE_SIZE = 20;
    //超过多少，跳转，屏幕的4分之1
    public int NAVI_BOUNDED = 4;

    //导航模式开启中
    public boolean inNavigation = false;
    //已确定事件分发
    protected boolean determined = false;
    //fragment帮助类
    public NavigationFragmentHelper mFragmentHelper;

    /**
     * 模式
     * 全屏or边
     * 全屏：目前没有解决与其它子控件的onLongClickListener冲突
     * 因为LongClick使用的时间的是move的eventTime和downTime的时间，没有走up，如果不给它down事件，其它的view的事件都会失效
     * 可以把执行longClick时先判断inNavigation,如果不是导航中，再去执行
     */
    public enum Mode {
        fullScreen, edge
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = new GestureDetector(this, new MyDetector());
        initScreenSize();
        mFragmentHelper = new NavigationFragmentHelper(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        initScreenSize();
        mNavigationToolbar = initNavigationToolbar();
        if (null != mNavigationToolbar) {
            mNavigationToolbar.mNavigationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void initScreenSize() {
        mScreenWidth = ScreenUtil.getScreenWidth(this);
        edgeSize = mScreenWidth / EDGE_SIZE;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    protected abstract NavigationToolbar initNavigationToolbar();

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        NavigationTouchListener listener = getLastTouchListener();
        if (null != listener && mListeners.size() > 1) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (inAnimator) {
                    return false;
                }
            }
            if (mMode == Mode.edge) {
                //如果是边模式，只跟据位置判断
                if (ev.getX() < mScreenWidth / EDGE_SIZE && ev.getAction() == MotionEvent.ACTION_DOWN) {
                    return onTouchEvent(ev);
                }
            } else if (mMode == Mode.fullScreen) {
                //如果滑到边，按边的模式
                if (ev.getX() < mScreenWidth / EDGE_SIZE && ev.getAction() == MotionEvent.ACTION_DOWN) {
                    return onTouchEvent(ev);
                }
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    mDownY = ev.getRawY();
                    mDownX = ev.getRawX();
                    //重执导航中和已确定方向为false
                    determined = false;
                    inNavigation = false;
                }
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    if (inNavigation) {
                        inNavigation = false;
                        return onTouchEvent(ev);
                    }
                    //重执导航中和已确定方向为false
                    determined = false;
                    inNavigation = false;
                    return super.dispatchTouchEvent(ev);
                }

                //判断滑动方向:滑动距离超过50dp
                if (!scrollDistance50dp(mDownX, mDownY, ev.getRawX(), ev.getRawY()) && !determined) {
                    //已确定方向
                    if (scrollDistance50dp(mDownX, mDownY, ev.getRawX(), ev.getRawY())) {
                        determined = true;
                    }
                    //判断方向，返回true表示确定是左右，返回false，不一定是确定完了，所以要用时间或距离进行确定，如果在时间距离之后，仍然没有确定是左右，那么就是上下了
                    if (mGestureDetector.onTouchEvent(ev)) {
                        inNavigation = true;
                        determined = true;
                        return onTouchEvent(ev);
                    } else {
                        return super.dispatchTouchEvent(ev);
                    }
                } else {
                    if (inNavigation) {
                        return onTouchEvent(ev);
                    }
                    return super.dispatchTouchEvent(ev);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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

    protected abstract void popBackStack();

    protected NavigationTouchListener getLastTouchListener() {
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
    protected void registerNavigationTouchListener(NavigationTouchListener listener) {
        mListeners.add(listener);
        viewChange(mListeners.size());
    }

    /**
     * 取消
     * 把Activity的touch事件传递给fragment
     */
    protected void unRegisterNavigationTouchListener(NavigationTouchListener listener) {
        mListeners.remove(listener);
    }

    /**
     * @return 滑动的距离超过50dp
     */
    protected boolean scrollDistance50dp(float downX, float downY, float x, float y) {
        return Math.abs(downX - x) > 50 || Math.abs(downY - y) > 50;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View currentView = getCurrentView();
            if (null != currentView) {
                if (currentView.getVisibility() != View.VISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }
        }
        NavigationTouchListener listener = getLastTouchListener();
        if (null != listener) {
            return listener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public abstract View getNavigationView();

    public abstract View getCurrentView();

    public abstract String getNavigationText();

    public abstract String getNextNavigationText();

    protected void backStack(final View currentView, final View popBackView) {
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
                    getFragmentManager().popBackStack();
                    viewChange(mListeners.size() - 1);
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
     * Start at page 1
     * @param page 从1页开始
     */
    public void viewChange(float page) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentHelper = null;
        mGestureDetector = null;
        mListeners = null;
        mNavigationToolbar = null;
    }

    private class MyDetector extends GestureDetector.SimpleOnGestureListener {
        /**
         * @return true: 右左 ；false: 上下
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) < Math.abs(distanceX);
        }
    }
}
