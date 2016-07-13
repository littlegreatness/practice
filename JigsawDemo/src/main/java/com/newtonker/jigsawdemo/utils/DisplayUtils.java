package com.newtonker.jigsawdemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtils
{
    /**
     * dp 转 px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp)
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return Math.round(dp * dm.densityDpi / 160.0F);
    }

    /**
     * 获取屏幕的宽度，像素值
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
