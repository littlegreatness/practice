package com.prac.buxiaoqing.meitudemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * author：buxiaoqing on 8/26/16 11:39
 * Just do IT(没有梦想,何必远方)
 */
public class OutScrollView extends ScrollView {
    private String TAG = OutScrollView.class.getSimpleName();


    public boolean isOnDrag() {
        return isOnDrag;
    }

    public void setOnDrag(boolean onDrag) {
        isOnDrag = onDrag;
    }

    private boolean isOnDrag;

    public OutScrollView(Context context) {
        super(context);
    }

    public OutScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            log("OutScrollView onInterceptTouchEvent ACTION_DOWN = " + super.onInterceptTouchEvent(ev));

        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            log("OutScrollView onInterceptTouchEvent ACTION_MOVE = " + super.onInterceptTouchEvent(ev));
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            log("OutScrollView onInterceptTouchEvent ACTION_UP = " + super.onInterceptTouchEvent(ev));

        }

        if (isOnDrag) {
            log("OutScrollView onInterceptTouchEvent isOnDrag = " + isOnDrag + "return false  ");
            return false;
        } else {
            log("OutScrollView onInterceptTouchEvent isOnDrag = " + isOnDrag + " return super.onInterceptTouchEvent(ev)  =  " + super.onInterceptTouchEvent(ev));
            return super.onInterceptTouchEvent(ev);
        }
    }


    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
