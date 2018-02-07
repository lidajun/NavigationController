package com.github.lidajun.android.navigationcontroller.activity_fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lidajun.android.navigationcontroller.listener.PopBackListener;
import com.github.lidajun.android.navigationcontroller.utils.DisplayUtil;

/**
 * Created by lidajun on 17-6-29.
 */

class NavigationFragmentHelper {
    private float mAnimatorTitleTvX = -1.0f;
    private float mAnimatorNavigationTvX = -1.0f;
    private float downX = -1.0f;
    private float mAnimatorTitleViewMaxTitleX = -1.0f;
    private TextView mAnimatorNavigationTv;
    private TextView mAnimatorTitleTv;
    private Rect mRect;
    private NavigationBaseActivity mActivity;

    NavigationFragmentHelper(NavigationBaseActivity activity) {
        mActivity = activity;
    }

    void created() {
        if (null != mActivity.mNavigationToolbar) {
            //把上一个view的标题做为这个view的导航目的地标题
            if (!TextUtils.isEmpty(mActivity.getNavigationText())) {
                mActivity.mNavigationToolbar.mBackTv.setText(String.format("%s%s", mActivity.backPrefix, mActivity.getNavigationText()));
            }
        }
    }

    void initToolbarNavigationText(String title) {
        if (null != mActivity.mNavigationToolbar) {
            if (!TextUtils.isEmpty(mActivity.getNavigationText())) {
                mActivity.mNavigationToolbar.mBackTv.setText(String.format("%s%s", mActivity.backPrefix, mActivity.getNavigationText()));
                mActivity.mNavigationToolbar.mBackTv.setAlpha(1.0f);
            } else {
                mActivity.mNavigationToolbar.mBackTv.setText("");
            }
            mActivity.mNavigationToolbar.mTitleTv.setText(title);
        }
    }

    private boolean mTouchMove = false;
    private boolean firstTouch = true;

    boolean touchEvent(final MotionEvent event, PopBackListener popBack) {
        //第一次进来并且不是DOWN就重置为DOWN，防止被其它view消费掉down
        if (firstTouch && event.getAction() != MotionEvent.ACTION_DOWN) {
            event.setAction(MotionEvent.ACTION_DOWN);
        }
        firstTouch = false;
        View currentView = mActivity.getCurrentView();
        if (null == currentView) {
            return false;
        }
        final View navigationView = mActivity.getNavigationView();
        if (null == navigationView) {
            return false;
        }
        if (mActivity.inAnimator) {
            return false;
        }
        if (null != mActivity.mNavigationToolbar) {
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
                if (currentView.getVisibility() != View.VISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
                downX = event.getX();
                if (downX > mActivity.edgeSize) {
                    //防止down的时候动画没做完
                    if (navigationView.getX() != 0 || currentView.getX() != 0 || mTouchMove) {
                        resetView(currentView, navigationView, popBack);
                    }
                    return false;
                }
                if (null != mActivity.mNavigationToolbar) {
                    //如果有导航文字
                    if (null != mActivity.mNavigationToolbar.mBackTv) {
                        showAnimatorView();
                    }
                    //如果有下一个导航文字
                    if (!TextUtils.isEmpty(mActivity.getNavigationBackText())) {
                        mActivity.mNavigationToolbar.mBackTv.setText(String.format("%s%s", mActivity.backPrefix, mActivity.getNavigationBackText()));
                    }
                }
                mTouchMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                if (downX <= 0) {
                    downX = moveX;
                }
                float vX;
                if (downX > mActivity.edgeSize) {
                    resetView(currentView, navigationView, popBack);
                    return false;
                }
                //禁止向左滑
                vX = event.getX() - downX;
                if (vX <= 0) {
                    vX = 0;
                }
                //防止两个view同时被滑动
                if (mTouchMove && navigationView.getX() == currentView.getX()) {
                    resetView(currentView, navigationView, popBack);
                    return true;
                }
                currentView.setX(vX);
                mTouchMove = true;
                mActivity.inNavigation = true;
                mActivity.viewChange(mActivity.mListeners.size() - vX / mActivity.mScreenWidth - 1);
                if (navigationView.getVisibility() != View.VISIBLE) {
                    navigationView.setVisibility(View.VISIBLE);
                }
                if (null != mActivity.mNavigationToolbar) {
                    if (null != mAnimatorNavigationTv) {
                        float x = mAnimatorNavigationTvX + vX / 2;
                        if (x >= mAnimatorTitleViewMaxTitleX) {
                            x = mAnimatorTitleViewMaxTitleX;
                        }
                        mAnimatorNavigationTv.setX(x);
                        mAnimatorNavigationTv.setAlpha((x) / mAnimatorTitleViewMaxTitleX);
                        //如果有一下个导航标题，就从0到1，否则1到0
                        if (!TextUtils.isEmpty(mActivity.getNavigationBackText())) {
                            mActivity.mNavigationToolbar.mBackTv.setAlpha((x) / mAnimatorTitleViewMaxTitleX);
                        } else {
                            mActivity.mNavigationToolbar.mBackTv.setAlpha((mAnimatorTitleTvX - vX / 2) / mAnimatorTitleTvX);
                        }
                    }
                    if (null != mAnimatorTitleTv) {
                        float x = mAnimatorTitleTvX + vX / 2;
                        mAnimatorTitleTv.setX(x);
                        mAnimatorTitleTv.setAlpha((mAnimatorTitleTvX - vX / 2) / mAnimatorTitleTvX);
                    }
                }
                navigationView.setX(-mActivity.mScreenWidth / 2 + vX / 2);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetView(currentView, navigationView, popBack);
                break;
        }
        return true;
    }

    /**
     * @param currentView    当前所在的view
     * @param navigationView 要导航到哪个view
     */
    private void startAnimator(View currentView, View navigationView, PopBackListener popBack) {
        mActivity.inAnimator = true;
        if (null == currentView || null == navigationView) {
            return;
        }
        float x = currentView.getX();
        float nTvX = 0;
        float tTvX = 0;
        if (null != mActivity.mNavigationToolbar && null != mAnimatorNavigationTv && null != mAnimatorTitleTv) {
            nTvX = mAnimatorNavigationTv.getX();
            tTvX = mAnimatorTitleTv.getX();
        }
        long animatorDuration = 100;
        //切换视图
        if (x > mActivity.mScreenWidth / mActivity.NAVI_BOUNDED) {
            startValueAnimator(currentView, x, mActivity.mScreenWidth, animatorDuration, popBack);
            startValueAnimator(navigationView, -mActivity.mScreenWidth / 2 + x / 2, 0, animatorDuration, popBack);
            if (null != mActivity.mNavigationToolbar) {
                if (mAnimatorTitleViewMaxTitleX > nTvX) {
                    startTitleAnimator(mAnimatorNavigationTv, nTvX, mAnimatorTitleViewMaxTitleX, animatorDuration);
                }
                startTitleAnimator(mAnimatorTitleTv, tTvX, mActivity.mScreenWidth, animatorDuration);
                startAlphaAnimator(mAnimatorTitleTv, (mAnimatorTitleTvX - tTvX / 2) / mAnimatorTitleTvX, 0.0f, animatorDuration);
                if (TextUtils.isEmpty(mActivity.getNavigationText())) {
                    startAlphaAnimator(mActivity.mNavigationToolbar.mBackTv, (mAnimatorNavigationTvX + nTvX / 2) / mAnimatorTitleViewMaxTitleX, 0.0f, animatorDuration);
                }
            }
        } else {
            //不切换视图
            startValueAnimator(currentView, x, 0, animatorDuration, popBack);
            startValueAnimator(navigationView, -mActivity.mScreenWidth / 2 + x / 2, -mActivity.mScreenWidth / 2, animatorDuration, popBack);
            if (null != mActivity.mNavigationToolbar) {
                startTitleAnimator(mAnimatorNavigationTv, nTvX, mAnimatorNavigationTvX, animatorDuration);
                startTitleAnimator(mAnimatorTitleTv, tTvX, mAnimatorTitleTvX, animatorDuration);
                startAlphaAnimator(mAnimatorNavigationTv, (mAnimatorNavigationTvX + nTvX / 2) / mAnimatorTitleViewMaxTitleX, 0.0f, animatorDuration);
            }
        }
    }

    private void startAlphaAnimator(final View view, float form, final float to, long duration) {
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
    private void startValueAnimator(final View view, float form, final float to, long duration, final PopBackListener popBack) {
        ValueAnimator animator = ObjectAnimator.ofFloat(form, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (null != view) {
                    float value = (float) animation.getAnimatedValue();
                    view.setX(value);
                    if (view == mActivity.getCurrentView() && (to == mActivity.mScreenWidth || to == 0)) {
                        mActivity.viewChange(mActivity.mListeners.size() - value / mActivity.mScreenWidth - 1);
                    }
                    //切换视图
                    if (value >= mActivity.mScreenWidth) {
                        if (!TextUtils.isEmpty(mActivity.getNavigationText()) && null != mActivity.mNavigationToolbar) {
                            mActivity.mNavigationToolbar.mTitleTv.setText(mActivity.getNavigationText());
                        }
                        if (!TextUtils.isEmpty(mActivity.getNavigationBackText()) && null != mActivity.mNavigationToolbar) {
                            mActivity.mNavigationToolbar.mBackTv.setVisibility(View.VISIBLE);
                        }
                        dismissAnimatorView();
                        popBack.popBack();
                        //不切换视图
                    } else if (to == -mActivity.mScreenWidth >> 1 && value == to) {
                        if (!TextUtils.isEmpty(mActivity.getNavigationText()) && null != mActivity.mNavigationToolbar) {
                            mActivity.mNavigationToolbar.mBackTv.setVisibility(View.VISIBLE);
                        }
                        dismissAnimatorView();
                        view.setX(0);
                        view.setVisibility(View.GONE);
                    }
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    private void showAnimatorView() {
        if (null == mAnimatorNavigationTv) {
            if (!TextUtils.isEmpty(mActivity.getNavigationText())) {
                mAnimatorNavigationTv = new TextView(mActivity);
                mAnimatorNavigationTv.setTextColor(mActivity.mNavigationToolbar.mTitleTv.getTextColors());
                mAnimatorNavigationTv.setTextSize(DisplayUtil.px2sp(mActivity, mActivity.mNavigationToolbar.mTitleTv.getTextSize()));
                mAnimatorNavigationTvX = mActivity.mNavigationToolbar.mBackTv.getX();
                mAnimatorNavigationTv.setX(mAnimatorNavigationTvX);
                mAnimatorNavigationTv.setAlpha(0);
                mAnimatorNavigationTv.setY(mActivity.mNavigationToolbar.mTitleTv.getY());
                mAnimatorNavigationTv.setText(mActivity.getNavigationText());
                if (null == mRect) {
                    mRect = new Rect();
                }
                //计算出下文字的最大x位置
                mAnimatorNavigationTv.getPaint().getTextBounds(mAnimatorNavigationTv.getText().toString(), (int) mActivity.mNavigationToolbar.getX() + mActivity.mNavigationToolbar.mTitleTv.getPaddingLeft(), mAnimatorNavigationTv.getText().toString().length(), mRect);
                mAnimatorTitleViewMaxTitleX = (mActivity.mScreenWidth - mRect.right) >> 1;
                mActivity.getWindow().addContentView(mAnimatorNavigationTv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        if (null == mAnimatorTitleTv) {
            mAnimatorTitleTv = new TextView(mActivity);
            mAnimatorTitleTv.setLayoutParams(mActivity.mNavigationToolbar.mTitleTv.getLayoutParams());
            mAnimatorTitleTv.setTextColor(mActivity.mNavigationToolbar.mTitleTv.getTextColors());
            mAnimatorTitleTv.setTextSize(DisplayUtil.px2sp(mActivity, mActivity.mNavigationToolbar.mTitleTv.getTextSize()));
            mAnimatorTitleTv.setText(mActivity.mNavigationToolbar.mTitleTv.getText().toString());
            mAnimatorTitleTvX = mActivity.mNavigationToolbar.mTitleTv.getX();
            mAnimatorTitleTv.setX(mAnimatorTitleTvX);
            mAnimatorTitleTv.setY(mActivity.mNavigationToolbar.mTitleTv.getY());
            mActivity.getWindow().addContentView(mAnimatorTitleTv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mActivity.mNavigationToolbar.mTitleTv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 把动画的view消失
     */
    private void dismissAnimatorView() {
        if (null != mActivity.mNavigationToolbar && mActivity.mNavigationToolbar.mTitleTv.getVisibility() != View.VISIBLE) {
            mActivity.mNavigationToolbar.mTitleTv.setVisibility(View.VISIBLE);
        }
        mActivity.inAnimator = false;
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
    private void resetView(View currentView, View navigationView, PopBackListener popBack) {
        firstTouch = true;
        downX = -1.0f;
        if (navigationView.getVisibility() == View.VISIBLE) {
            mActivity.inAnimator = true;
            startAnimator(currentView, navigationView, popBack);
        }
    }

}
