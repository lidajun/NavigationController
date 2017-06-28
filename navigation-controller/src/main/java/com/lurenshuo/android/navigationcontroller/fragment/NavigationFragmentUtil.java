package com.lurenshuo.android.navigationcontroller.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lurenshuo.android.navigationcontroller.activity.NavigationActivityV4;
import com.lurenshuo.android.navigationcontroller.activity.NavigationBaseActivity;
import com.lurenshuo.android.navigationcontroller.listener.PopBackListener;
import com.lurenshuo.android.navigationcontroller.utils.DisplayUtil;

/**
 * Created by lidajun on 17-6-22.
 */

public class NavigationFragmentUtil {

    private static float mAnimatorTitleTvX = -1.0f;
    private static float mAnimatorNavigationTvX = -1.0f;
    private static float downX = -1.0f;
    private static float mAnimatorTitleViewMaxTitleX = -1.0f;
    private static TextView mAnimatorNavigationTv;
    private static TextView mAnimatorTitleTv;
    private static Rect mRect;

    static void created(NavigationBaseActivity activity) {
        if (null != activity.mNavigationToolbar) {
            //把上一个view的标题做为这个view的导航目的地标题
            if (!TextUtils.isEmpty(activity.getNavigationText())) {
                activity.mNavigationToolbar.mNavigationTv.setText(String.format("<%s", activity.getNavigationText()));
            }
        }
    }

    static void initToolbarNavigationText(NavigationBaseActivity activity, String title) {
        if (null != activity.mNavigationToolbar) {
            if (!TextUtils.isEmpty(activity.getNavigationText())) {
                activity.mNavigationToolbar.mNavigationTv.setText(String.format("<%s", activity.getNavigationText()));
                activity.mNavigationToolbar.mNavigationTv.setAlpha(1.0f);
            } else {
                activity.mNavigationToolbar.mNavigationTv.setText("");
            }
            activity.mNavigationToolbar.mTitleTv.setText(title);
        }
    }

    static boolean touchEvent(final MotionEvent event, NavigationBaseActivity activity, PopBackListener popBack) {
        View currentView = activity.getCurrentView();
        if (null == currentView) {
            return false;
        }
        final View navigationView = activity.getNavigationView();
        if (null == navigationView) {
            return false;
        }
        if (activity.inAnimator) {
            return false;
        }
        if (null != activity.mNavigationToolbar) {
            if (null == mRect) {
                mRect = new Rect();
                currentView.getGlobalVisibleRect(mRect);
            }
            //如果触摸点不在当前view的范围内，直接return
            if (event.getRawY() < mRect.top && navigationView.getVisibility() == View.GONE) {
                return false;
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                if (downX > activity.edgeSize) {
                    return false;
                }
                if (null != activity.mNavigationToolbar) {
                    //如果有导航文字
                    if (null != activity.mNavigationToolbar.mNavigationTv) {
                        showAnimatorView(activity);
                    }
                    //如果有下一个导航文字
                    if (!TextUtils.isEmpty(activity.getNextNavigationText())) {
                        activity.mNavigationToolbar.mNavigationTv.setText(String.format("<%s", activity.getNextNavigationText()));
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                if (downX <= 0) {
                    downX = moveX;
                }
                float vX;
                if (activity.mMode == NavigationActivityV4.Mode.edge) {
                    if (downX > activity.edgeSize) {
                        resetView(currentView, navigationView, activity, popBack);
                        return false;
                    }
                }
                //禁止向左滑
                vX = event.getX() - downX;
                if (vX <= 0) {
                    vX = 0;
                }
                currentView.setX(vX);
                if (navigationView.getVisibility() != View.VISIBLE) {
                    navigationView.setVisibility(View.VISIBLE);
                }
                if (null != activity.mNavigationToolbar) {
                    if (null != mAnimatorNavigationTv) {
                        float x = mAnimatorNavigationTvX + vX / 2;
                        if (x >= mAnimatorTitleViewMaxTitleX) {
                            x = mAnimatorTitleViewMaxTitleX;
                        }
                        mAnimatorNavigationTv.setX(x);
                        mAnimatorNavigationTv.setAlpha((x) / mAnimatorTitleViewMaxTitleX);
                        //如果有一下个导航标题，就从0到1，否则1到0
                        if (!TextUtils.isEmpty(activity.getNextNavigationText())) {
                            activity.mNavigationToolbar.mNavigationTv.setAlpha((x) / mAnimatorTitleViewMaxTitleX);
                        } else {
                            activity.mNavigationToolbar.mNavigationTv.setAlpha((mAnimatorTitleTvX - vX / 2) / mAnimatorTitleTvX);
                        }
                    }
                    if (null != mAnimatorTitleTv) {
                        float x = mAnimatorTitleTvX + vX / 2;
                        mAnimatorTitleTv.setX(x);
                        mAnimatorTitleTv.setAlpha((mAnimatorTitleTvX - vX / 2) / mAnimatorTitleTvX);
                    }
                }
                navigationView.setX(-activity.mScreenWidth / 2 + vX / 2);
                break;
            case MotionEvent.ACTION_UP:
                resetView(currentView, navigationView, activity, popBack);
                break;
        }
        return true;
    }

    /**
     * @param currentView    当前所在的view
     * @param navigationView 要导航到哪个view
     */
    private static void startAnimator(View currentView, View navigationView, NavigationBaseActivity activity, PopBackListener popBack) {
        activity.inAnimator = true;
        if (null == currentView || null == navigationView) {
            return;
        }
        float x = currentView.getX();
        float nTvX = 0;
        float tTvX = 0;
        if (null != activity.mNavigationToolbar && null != mAnimatorNavigationTv && null != mAnimatorTitleTv) {
            nTvX = mAnimatorNavigationTv.getX();
            tTvX = mAnimatorTitleTv.getX();
        }
        long animatorDuration = 100;
        //切换视图
        if (x > activity.mScreenWidth / activity.NAVI_BOUNDED) {
            startValueAnimator(currentView, x, activity.mScreenWidth, animatorDuration, activity, popBack);
            startValueAnimator(navigationView, -activity.mScreenWidth / 2 + x / 2, 0, animatorDuration, activity, popBack);
            if (null != activity.mNavigationToolbar) {
                if (nTvX < mAnimatorTitleViewMaxTitleX) {
                    startTitleAnimator(mAnimatorNavigationTv, nTvX, mAnimatorTitleViewMaxTitleX, animatorDuration);
                }
                startTitleAnimator(mAnimatorTitleTv, tTvX, activity.mScreenWidth, animatorDuration);
                startAlphaAnimator(mAnimatorTitleTv, (mAnimatorTitleTvX - tTvX / 2) / mAnimatorTitleTvX, 0.0f, animatorDuration);
                if (TextUtils.isEmpty(activity.getNavigationText())) {
                    startAlphaAnimator(activity.mNavigationToolbar.mNavigationTv, (mAnimatorNavigationTvX + nTvX / 2) / mAnimatorTitleViewMaxTitleX, 0.0f, animatorDuration);
                }
            }
        } else {
            //不切换视图
            startValueAnimator(currentView, x, 0, animatorDuration, activity, popBack);
            startValueAnimator(navigationView, -activity.mScreenWidth / 2 + x / 2, -activity.mScreenWidth / 2, animatorDuration, activity, popBack);
            if (null != activity.mNavigationToolbar) {
                startTitleAnimator(mAnimatorNavigationTv, nTvX, mAnimatorNavigationTvX, animatorDuration);
                startTitleAnimator(mAnimatorTitleTv, tTvX, mAnimatorTitleTvX, animatorDuration);
                startAlphaAnimator(mAnimatorNavigationTv, (mAnimatorNavigationTvX + nTvX / 2) / mAnimatorTitleViewMaxTitleX, 0.0f, animatorDuration);
            }
        }
    }

    private static void startAlphaAnimator(final View view, float form, final float to, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(form, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (null != view) {
                    view.setAlpha(value);
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    private static void startTitleAnimator(final View view, float form, final float to, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(form, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (null != view) {
                    view.setX(value);
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * 做一个动画
     *
     * @param view     目标
     * @param form     form
     * @param to       to
     * @param duration 时长
     */
    @NonNull
    private static void startValueAnimator(final View view, float form, final float to, long duration, final NavigationBaseActivity activity, final PopBackListener popBack) {
        ValueAnimator animator = ObjectAnimator.ofFloat(form, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (null != view) {
                    float value = (float) animation.getAnimatedValue();
                    view.setX(value);
                    //切换视图
                    if (value >= activity.mScreenWidth) {
                        if (!TextUtils.isEmpty(activity.getNavigationText())) {
                            activity.mNavigationToolbar.mTitleTv.setText(activity.getNavigationText());
                        }
                        if (!TextUtils.isEmpty(activity.getNextNavigationText())) {
                            activity.mNavigationToolbar.mNavigationTv.setVisibility(View.VISIBLE);
                        }
                        dismissAnimatorView(activity);
                        popBack.popBack();
                        //不切换视图
                    } else if (to == -activity.mScreenWidth / 2 && value == to) {
                        if (!TextUtils.isEmpty(activity.getNavigationText())) {
                            activity.mNavigationToolbar.mNavigationTv.setVisibility(View.VISIBLE);
                        }
                        dismissAnimatorView(activity);
                        view.setX(0);
                        view.setVisibility(View.GONE);
                    }
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    private static void showAnimatorView(NavigationBaseActivity activity) {
        if (null == mAnimatorNavigationTv) {
            if (!TextUtils.isEmpty(activity.getNavigationText())) {
                mAnimatorNavigationTv = new TextView(activity);
                mAnimatorNavigationTv.setTextColor(activity.mNavigationToolbar.mTitleTv.getTextColors());
                mAnimatorNavigationTv.setTextSize(DisplayUtil.px2sp(activity, activity.mNavigationToolbar.mTitleTv.getTextSize()));
                mAnimatorNavigationTvX = activity.mNavigationToolbar.mNavigationTv.getX();
                mAnimatorNavigationTv.setX(mAnimatorNavigationTvX);
                mAnimatorNavigationTv.setAlpha(0);
                mAnimatorNavigationTv.setY(activity.mNavigationToolbar.mTitleTv.getY());
                mAnimatorNavigationTv.setText(activity.getNavigationText());
                if (null == mRect) {
                    mRect = new Rect();
                }
                //计算出下文字的最大x位置
                mAnimatorNavigationTv.getPaint().getTextBounds(mAnimatorNavigationTv.getText().toString(), 0, mAnimatorNavigationTv.getText().toString().length(), mRect);
                mAnimatorTitleViewMaxTitleX = activity.mScreenWidth / 2 - mRect.centerX() - mRect.left;
                activity.getWindow().addContentView(mAnimatorNavigationTv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        if (null == mAnimatorTitleTv) {
            mAnimatorTitleTv = new TextView(activity);
            mAnimatorTitleTv.setLayoutParams(activity.mNavigationToolbar.mTitleTv.getLayoutParams());
            mAnimatorTitleTv.setTextColor(activity.mNavigationToolbar.mTitleTv.getTextColors());
            mAnimatorTitleTv.setTextSize(DisplayUtil.px2sp(activity, activity.mNavigationToolbar.mTitleTv.getTextSize()));
            mAnimatorTitleTv.setText(activity.mNavigationToolbar.mTitleTv.getText().toString());
            mAnimatorTitleTvX = activity.mNavigationToolbar.mTitleTv.getX();
            mAnimatorTitleTv.setX(mAnimatorTitleTvX);
            mAnimatorTitleTv.setY(activity.mNavigationToolbar.mTitleTv.getY());
            activity.getWindow().addContentView(mAnimatorTitleTv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            activity.mNavigationToolbar.mTitleTv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 把动画的view消失
     */
    private static void dismissAnimatorView(NavigationBaseActivity activity) {
        if (null != activity.mNavigationToolbar && activity.mNavigationToolbar.mTitleTv.getVisibility() != View.VISIBLE) {
            activity.mNavigationToolbar.mTitleTv.setVisibility(View.VISIBLE);
        }
        activity.inAnimator = false;
        if (null != mAnimatorNavigationTv) {
            mAnimatorNavigationTv.setText("");
            mAnimatorNavigationTv = null;
        }
        if (null != mAnimatorTitleTv) {
            mAnimatorTitleTv.setText("");
            mAnimatorTitleTv = null;
        }
        if (null != mRect) {
            mRect = null;
        }
        mAnimatorTitleViewMaxTitleX = -1.0f;
        mAnimatorTitleTvX = -1.0f;
        mAnimatorNavigationTvX = -1.0f;
        downX = -1.0f;
    }

    /**
     * 把view重置
     */
    private static void resetView(View currentView, View navigationView, NavigationBaseActivity activity, PopBackListener popBack) {
        downX = -1.0f;
        if (navigationView.getVisibility() == View.VISIBLE) {
            activity.inAnimator = true;
            NavigationFragmentUtil.startAnimator(currentView, navigationView, activity, popBack);
        }
    }
}
