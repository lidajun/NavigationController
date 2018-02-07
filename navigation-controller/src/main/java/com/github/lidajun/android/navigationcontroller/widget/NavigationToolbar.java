package com.github.lidajun.android.navigationcontroller.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.github.lidajun.android.navigationcontroller.R;
import com.github.lidajun.android.navigationcontroller.utils.DisplayUtil;

/**
 * Created by lidajun on 17-6-23.
 */

public class NavigationToolbar extends Toolbar {

    public TextView mTitleTv;
    public TextView mBackTv;

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
        mBackTv = new TextView(context);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mTitleTv.setLayoutParams(layoutParams);
        mBackTv.setGravity(Gravity.CENTER_VERTICAL);
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.navigation_toolbar_attrs);
        int titleTextColor = typedArray.getColor(R.styleable.navigation_toolbar_attrs_title_view_text_color, Color.BLACK);
        mTitleTv.setTextColor(titleTextColor);
        int backTextColor = typedArray.getColor(R.styleable.navigation_toolbar_attrs_back_view_text_color, Color.BLACK);
        mBackTv.setTextColor(backTextColor);
        float backTextSize = typedArray.getFloat(R.styleable.navigation_toolbar_attrs_back_view_text_size, 18);
        mBackTv.setTextSize(backTextSize);
        float titleTextSize = typedArray.getFloat(R.styleable.navigation_toolbar_attrs_title_view_text_size, 20);
        mTitleTv.setTextSize(titleTextSize);
        typedArray.recycle();
        mBackTv.setX(DisplayUtil.dip2px(context, 10));
        this.addView(mTitleTv);
        this.addView(mBackTv);
        setTitle("");
    }
}
