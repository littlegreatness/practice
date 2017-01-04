package com.prac.buxiaoqing.greendao.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * author：buxiaoqing on 2016/12/29 15:20
 * Just do IT(没有梦想,何必远方)
 */
public class MyTransformation extends BitmapTransformation {
    private static float radius = 4f;

    public MyTransformation(Context context, float r) {
        super(context);
        this.radius = r;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getWidth() - size) / 2;
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas c = new Canvas(result);
        Paint p = new Paint();
        p.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        p.setAntiAlias(true);
        float r = size / 2f;
        c.drawCircle(r, r, r, p);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
