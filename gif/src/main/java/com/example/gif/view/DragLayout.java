package com.example.gif.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by buxiaoqing on 16/6/28.
 */
public class DragLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private GestureDetectorCompat detectorCompat;
    private MainLayout mainLayout;
    private RelativeLayout mMenuLayout;

    private State mState = State.CLOSE;

    private int mDragRange;//最大滑动距离
    private int mMenuWidth;
    private int mMenuHeight;
    private int mMainLeftRange;  //当前滑动距离

    private DragListener dragListener;

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public interface DragListener {
        void open();

        void close();

        void drag(float percent);

    }

    private ViewDragHelper.Callback dragCallBack = new ViewDragHelper.Callback() {

        //开始捕获view
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mainLayout == child;//如果当前child是mMainLayout时开始检测
        }

        //在滑动的时候对当前view的一些限制   left,手势滑动的距离,dx实际作用的距离
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            if (mMainLeftRange + dx < 0) {
                return 0;//close
            } else if (mMainLeftRange + dx > mDragRange) {
                return mDragRange;//完全打开的时候
            } else {
                return left;//滑动中,没有触发上述两个条件
            }
        }

        //释放   xvel表示滑动速率,大于0表示向左,小于0向右    yvel同理
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //向左滑动并且滑动距离大于mDragRange * 0.3才打开
            if (releasedChild == mainLayout && xvel > 0 && mMainLeftRange > mDragRange * 0.3) {
                open();
            } else {
                close();
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mMenuWidth;
        }

        //滑动的时候,view发生变化时调用   left,top  view的新位置     dx,dy发生变动的距离
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if (changedView == mainLayout) {
                mMainLeftRange = left;
            } else {
                mMainLeftRange += left;
            }

            if (mMainLeftRange > mDragRange) {
                mMainLeftRange = mDragRange;
            } else if (mMainLeftRange < 0) {
                mMainLeftRange = 0;
            }

            if (changedView == mMenuLayout) {
                mMenuLayout.layout(0, 0, mMenuWidth, mMenuHeight);
                mainLayout.layout(mMainLeftRange, 0, mMainLeftRange + mMenuWidth, mMenuHeight);
            }
            dragEvent(mMainLeftRange);
        }
    };

    private void dragEvent(int mMainLeftRange) {
        if (dragListener == null) {
            return;
        }
        float percent = mMainLeftRange / mDragRange;
        animateView(percent);
        State lastState = mState;
        if (lastState != getmState() && mState == State.CLOSE) {
            dragListener.close();
        } else if (lastState != getmState() && mState == State.OPEN) {
            dragListener.open();
        } else {
            dragListener.drag(mMainLeftRange);
        }
    }

    private void animateView(float percent) {

        float f1 = (float) (1 - percent * 0.3);
        ViewHelper.setScaleX(mainLayout, f1);
        ViewHelper.setScaleY(mainLayout, f1);

        ViewHelper.setTranslationX(mMenuLayout, mMenuLayout.getWidth() * percent / 2.3f - mMenuLayout.getWidth() / 2.3f);
        ViewHelper.setScaleX(mMenuLayout, (float) (0.5 + 0.5 * percent));
        ViewHelper.setScaleY(mMenuLayout, (float) (0.5 + 0.5 * percent));

        ViewHelper.setAlpha(mMenuLayout, percent);

        if (getBackground() != null) {
            getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
        }
    }

    private Integer evaluate(float percent, int black, int transparent) {
        int startA = (black >> 24) & 0xff;
        int startR = (black >> 16) & 0xff;
        int startG = (black >> 8) & 0xff;
        int startB = (black) & 0xff;

        int endA = (transparent >> 24) & 0xff;
        int endR = (transparent >> 16) & 0xff;
        int endG = (transparent >> 8) & 0xff;
        int endB = (transparent) & 0xff;

        return (int) (startA + (int) percent * (endA - startA) << 24) |
                (int) (startR + (int) percent * (endR - startR) << 16) |
                (int) (startG + (int) percent * (endG - startG) << 8) |
                (int) (startB + (int) percent * (endB - startB));
    }


    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        viewDragHelper = ViewDragHelper.create(this, dragCallBack);
        detectorCompat = new GestureDetectorCompat(context, new MSimpleOnGestureListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuLayout = (RelativeLayout) getChildAt(0);
        mMenuLayout.setClickable(true);
        mainLayout = (MainLayout) getChildAt(1);
        mainLayout.setDragLayout(this);
        mainLayout.setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化view的大小
        mMenuWidth = mMenuLayout.getMeasuredWidth();
        mMenuHeight = mMenuLayout.getMeasuredHeight();
        mDragRange = (int) (mMenuWidth * 0.6);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMenuLayout.layout(0, 0, mMenuWidth, mMenuHeight);
        mainLayout.layout(mMainLeftRange, 0, mMenuWidth + mMainLeftRange, mMenuHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //由两者共同决定是否阻断事件
        return viewDragHelper.shouldInterceptTouchEvent(ev) && detectorCompat.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //用viewDragHelper处理
        viewDragHelper.processTouchEvent(event);
        return false;
    }

    public State getmState() {
        if (mMainLeftRange == 0) {
            mState = State.CLOSE;
        } else if (mMainLeftRange == mDragRange) {
            mState = State.OPEN;
        } else {
            mState = State.DRAGING;
        }
        return mState;
    }

    public void open() {
        if (viewDragHelper.smoothSlideViewTo(mainLayout, mDragRange, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void close() {
        if (viewDragHelper.smoothSlideViewTo(mainLayout, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    enum State {
        OPEN, CLOSE, DRAGING
    }

    class MSimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //横向滑动为主
            return Math.abs(distanceY) <= Math.abs(distanceX);
        }
    }
}
