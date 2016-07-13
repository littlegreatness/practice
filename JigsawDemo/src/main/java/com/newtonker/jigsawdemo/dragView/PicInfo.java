package com.newtonker.jigsawdemo.dragView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.newtonker.jigsawdemo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * author：buxiaoqing on 16/7/7 16:31
 * Just do IT(没有梦想,何必远方)
 */
public class PicInfo extends ImageView {

    private String picUrl;
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int width, height;//总的宽度是定了的,根据高宽比获取的高和宽
    private float ratio;//高和宽的比

    public PicInfo(Context context) {
        this(context, null);
    }

    public PicInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        //.thumbnail(0.1f)
        Glide.with(getContext()).load(new File(picUrl)).centerCrop().placeholder(R.mipmap.ic_photo_black_48dp)
                .error(R.mipmap.ic_broken_image_black_48dp).into(this);
        calRatio();
    }

    public String getPicUrl() {
        return picUrl;
    }

    private void calRatio() {
        File file = new File(picUrl);
        if (file.exists() && !file.isDirectory()) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                InputStream is = new FileInputStream(picUrl);
                BitmapFactory.decodeStream(is, null, options);
                this.ratio = options.outHeight / options.outWidth;
            } catch (Exception e) {
                Log.w("PicInfo", "getImageWH Exception.", e);
            }
        }
    }


    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
