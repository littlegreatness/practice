package com.example.gif.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by buxiaoqing on 16/6/28.
 */
public class MainLayout extends RelativeLayout {

    private DragLayout dragLayout;

    public void setDragLayout(DragLayout dragLayout) {
        this.dragLayout = dragLayout;
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //TODO
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragLayout.getmState() != DragLayout.State.CLOSE) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dragLayout.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (dragLayout.getmState() != DragLayout.State.CLOSE)
            return true;


        return super.onInterceptTouchEvent(ev);
    }
}
