package com.prac.buxiaoqing.greendao.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * author：buxiaoqing on 2016/12/29 14:57
 * Just do IT(没有梦想,何必远方)
 */
public class MyGlideModule implements GlideModule {


    public MyGlideModule() {
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
