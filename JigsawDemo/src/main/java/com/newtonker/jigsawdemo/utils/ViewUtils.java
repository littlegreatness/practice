package com.newtonker.jigsawdemo.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ViewUtils
{
    /**
     * 动画将视图显示
     */
    public static void viewFadeIn(Context c, View view, int duration)
    {
        if(View.VISIBLE == view.getVisibility())
        {
            return;
        }

        if (duration != 0)
        {
            Animation fadeAnimation = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
            fadeAnimation.setDuration(duration);
            view.startAnimation(fadeAnimation);
        }
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 用于隐藏view
     * c
     * view     将view隐藏
     * duration 动画时长
     */
    public static void viewFadeOut(Context c, View view, int duration)
    {
        if (duration != 0 && view.getVisibility() == View.VISIBLE)
        {
            Animation fadeAnimation = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
            fadeAnimation.setDuration(duration);
            view.startAnimation(fadeAnimation);
        }

        view.setVisibility(View.INVISIBLE);
    }
}
