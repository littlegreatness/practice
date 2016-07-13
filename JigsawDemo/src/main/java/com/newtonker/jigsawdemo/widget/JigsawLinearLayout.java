package com.newtonker.jigsawdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class JigsawLinearLayout extends LinearLayout
{
    private OnSelectedStateChangedListener onSelectedStateChangedListener;

    public JigsawLinearLayout(Context context)
    {
        super(context);
    }

    public JigsawLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public JigsawLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if(MotionEvent.ACTION_DOWN == ev.getAction() && null != onSelectedStateChangedListener)
        {
            onSelectedStateChangedListener.onSelectedStateChanged();
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void setOnSelectedStateChangedListener(OnSelectedStateChangedListener onSelectedStateChangedListener)
    {
        this.onSelectedStateChangedListener = onSelectedStateChangedListener;
    }

    public interface OnSelectedStateChangedListener
    {
        void onSelectedStateChanged();
    }
}
