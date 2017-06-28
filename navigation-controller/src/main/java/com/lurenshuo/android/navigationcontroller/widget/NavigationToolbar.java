package com.lurenshuo.android.navigationcontroller.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.lurenshuo.android.navigationcontroller.utils.DisplayUtil;
import com.lurenshuo.navigationcontroller.R;

/**
 * Created by lidajun on 17-6-23.
 */

public class NavigationToolbar extends Toolbar {

    public TextView mTitleTv;
    public TextView mNavigationTv;

    public NavigationToolbar(Context context) {
        this(context, null);
    }

    public NavigationToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTitleTv = new TextView(context);
        mNavigationTv = new TextView(context);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mTitleTv.setLayoutParams(layoutParams);
        mNavigationTv.setGravity(Gravity.CENTER_VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.navigation_toolbar_attrs);
        float naviTextSize = typedArray.getFloat(R.styleable.navigation_toolbar_attrs_navi_view_text_size, 18);
        mNavigationTv.setTextSize(naviTextSize);
        float titleTextSize = typedArray.getFloat(R.styleable.navigation_toolbar_attrs_title_view_text_size, 20);
        mTitleTv.setTextSize(titleTextSize);
        typedArray.recycle();
        mNavigationTv.setX(DisplayUtil.dip2px(context, 10));
        this.addView(mTitleTv);
        this.addView(mNavigationTv);
        setTitle("");
    }
}
