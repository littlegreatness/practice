package com.prac.buxiaoqing.prac.gif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * author：buxiaoqing on 2016/11/28 17:27
 * Just do IT(没有梦想,何必远方)
 */
public class CusProgressView extends View {


    private String titleA = "没劲";
    private String titleB = "顶上去";
    private int numA = 30;
    private int numB = 70;

    private Context mContext;
    private Paint mPaint;

    private int width, height;

    public CusProgressView(Context context) {
        this(context, null);
    }

    public CusProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, height / 2, (float) (0.3 * width), height / 2, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawLine((float) (0.3 * width), height / 2, width, height / 2, mPaint);
    }
}
