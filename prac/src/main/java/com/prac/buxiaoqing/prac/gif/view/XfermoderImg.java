package com.prac.buxiaoqing.prac.gif.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.prac.buxiaoqing.prac.R;

/**
 * author：buxiaoqing on 2016/11/15 17:33
 * Just do IT(没有梦想,何必远方)
 */
public class XfermoderImg extends ImageView {

    private Paint mPaint;

    public XfermoderImg(Context context) {
        super(context);
        init();
    }

    public XfermoderImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XfermoderImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    int width;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        width = w;
        setMeasuredDimension(w, h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.GREEN);
        canvas.translate(20, 20);
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.xin);

        Bitmap mask = Bitmap.createBitmap(width, width, src.getConfig());


        Canvas canvas1 = new Canvas(mask);
        canvas1.drawCircle(width / 2, width / 2, width / 2, mPaint);

        int src1 = canvas.saveLayer(0, 0, width, width, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(src, 0, 0, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(mask, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(src1);


    }
}
