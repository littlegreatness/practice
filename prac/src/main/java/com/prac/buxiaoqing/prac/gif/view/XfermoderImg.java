package com.prac.buxiaoqing.prac.gif.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.prac.buxiaoqing.prac.R;

/**
 * author：buxiaoqing on 2016/11/15 17:33
 * Just do IT(没有梦想,何必远方)
 */
public class XfermoderImg extends ImageView {

    private Bitmap src;
    private Bitmap out;
    private int width;
    private int height;

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
        // 禁止硬件加速，硬件加速会有一些问题，这里禁用掉
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //自己计算控件的宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int imgWidth = src.getWidth() + getPaddingLeft()
                    + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(widthSize, imgWidth);
            } else {
                width = imgWidth;
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int imgHeight = src.getHeight() + getPaddingTop()
                    + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(heightSize, imgHeight);
            } else {
                height = imgHeight;
            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        src = BitmapFactory.decodeResource(getResources(), R.drawable.xin, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = 16;
        src = BitmapFactory.decodeResource(getResources(), R.drawable.xin, options);
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
        super.onDraw(canvas);
        xmodeImage();
        //把画好画的画布放到自定义的画板上面
        canvas.drawBitmap(out, 0, 0, null);
    }

    private void xmodeImage() {
        //根据原始的图片创建一个画布
        out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //创建一个画板，在画布的基础上
        Canvas canvas = new Canvas(out);
        //创建一个画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int min = Math.min(width, height);
        //开始画圆
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        //设置Xfermode画笔模式为SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //然后有画了一个图片，最终实现两个图像的叠加
        canvas.drawBitmap(src, 0, 0, paint);
    }
}
