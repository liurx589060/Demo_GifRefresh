/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © www.moresing.com. All Rights Reserved
 *
 */
package com.example.liurunxiong.demo_gifrefresh;

import android.app.Activity;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Yan Zhenjie on 2016/7/7.
 */
public class DisplayUtils {

    private static boolean isInitialize = false;
    /**
     * 屏幕宽度
     **/
    public static int screenWidth;
    /**
     * 屏幕高度
     **/
    public static int screenHeight;
    /**
     * 屏幕密度
     **/
    public static int screenDpi;
    /**
     * 逻辑密度, 屏幕缩放因子
     */
    public static float density = 1;
    /**
     * 字体缩放因子
     */
    public static float scaledDensity;

    /**
     * 初始化屏幕宽度和高度
     */
    public static void initScreen(Activity activity) {
        if (isInitialize) return;
        isInitialize = true;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metric = new DisplayMetrics();

        if (VERSION.SDK_INT >= 17) {
            display.getRealMetrics(metric);
        } else {
            display.getMetrics(metric);
        }

        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDpi = metric.densityDpi;
        density = metric.density;
        scaledDensity = metric.scaledDensity;
    }

    /**
     * 是否是横屏
     */
    public static boolean isHorizontal() {
        return screenWidth > screenHeight;
    }

    public static int px2dip(float inParam) {
        return (int) (inParam / density + 0.5F);
    }

    public static int dip2px(float inParam) {
        return (int) (inParam * density + 0.5F);
    }

    public static int px2sp(float inParam) {
        return (int) (inParam / density + 0.5F);
    }

    public static int sp2px(float inParam) {
        return (int) (inParam * density + 0.5F);
    }
}