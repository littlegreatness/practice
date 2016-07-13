package com.newtonker.jigsawdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class JigsawFrameLayout extends FrameLayout
{
    public JigsawFrameLayout(Context context)
    {
        super(context);
    }

    public JigsawFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public JigsawFrameLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return true;
    }
}
