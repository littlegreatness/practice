package com.prac.buxiaoqing.prac.gif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.prac.buxiaoqing.prac.gif.util.PixelUtil;

/**
 * author：buxiaoqing on 20/10/2016 09:40
 * Just do IT(没有梦想,何必远方)
 */
public class MImageView extends ImageView {


    public MImageView(Context context) {
        super(context);
    }

    public MImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("MImageView", "onMeasure = " + this.getLayoutParams().height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("MImageView", "onLayout = " + this.getLayoutParams().height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //this.getLayoutParams().height = PixelUtil.dp2px(getContext(), 300);
        Log.e("MImageView", "onDraw = " + this.getLayoutParams().height);
    }
}
