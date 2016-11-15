package com.prac.buxiaoqing.prac.gif.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：buxiaoqing on 2016/11/15 16:10
 * Just do IT(没有梦想,何必远方)
 */
public class BitmapShaderImg extends ImageView {

    private BitmapShader shader;

    private int mWidth;
    private int mRadius;

    private Paint mPaint;
    private Matrix mMtatrix;

    public BitmapShaderImg(Context context) {
        this(context, null);
    }

    public BitmapShaderImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapShaderImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMtatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //circle
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = mWidth / 2;
        setMeasuredDimension(mWidth, mWidth);
    }

    private void setBitmapShader() {

        Drawable drawable = getDrawable();

        if (drawable == null)
            return;

        Bitmap bitmap = drawable2Bitmap(drawable);

        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        float scale = mWidth * 1.0f / bSize;

        mMtatrix.setScale(scale, scale);

        shader.setLocalMatrix(mMtatrix);

        mPaint.setShader(shader);
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        } else {

            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(c);
            return b;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        setBitmapShader();

        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }
}
