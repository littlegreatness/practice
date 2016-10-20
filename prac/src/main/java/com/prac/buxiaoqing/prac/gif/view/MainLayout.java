package com.prac.buxiaoqing.prac.gif.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.prac.buxiaoqing.prac.gif.util.PixelUtil;

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
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (dragLayout.getmState() != DragLayout.State.CLOSE) {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                dragLayout.close();
//            }
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getLayoutParams().height = PixelUtil.dp2px(getContext(),50);
        Log.e("MainLayout", "onLayout = " + this.getLayoutParams().height);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getLayoutParams().height = PixelUtil.dp2px(getContext(),50);
        Log.e("MainLayout", "onMeasure = " + this.getLayoutParams().height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("MainLayout", "onDraw = " + this.getLayoutParams().height);
        getLayoutParams().height = PixelUtil.dp2px(getContext(), 50);
        Log.e("MainLayout", "onDraw = " + this.getLayoutParams().height);



    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.setMeasuredDimension(PixelUtil.dp2px(getContext(), 300),PixelUtil.dp2px(getContext(), 50));
//        getLayoutParams().height = PixelUtil.dp2px(getContext(), 50);
    }


    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        if (dragLayout.getmState() != DragLayout.State.CLOSE)
//            return true;
//
//
//        return super.onInterceptTouchEvent(ev);
//    }
}
