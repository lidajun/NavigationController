package com.lurenshuo.android.navigationcontroller.utils;

import android.content.Context;
import android.util.DisplayMetrics;


/**
 * Created by Li dajun
 * Date: 2016-03-11
 * Time: 14:22
 */
public class ScreenUtil {

    private static int sScreenWidth;

    /**
     * 获取屏幕尺寸
     */
    public static int getScreenWidth(Context context) {
        if (0 == sScreenWidth) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            // 屏幕宽（像素，如：480px）
            sScreenWidth = dm.widthPixels;
        }
        //        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
        return sScreenWidth;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
