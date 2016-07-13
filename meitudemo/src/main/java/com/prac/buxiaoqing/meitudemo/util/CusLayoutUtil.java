package com.prac.buxiaoqing.meitudemo.util;


import android.graphics.BitmapFactory;


/**
 * author：buxiaoqing on 16/7/11 14:45
 * Just do IT(没有梦想,何必远方)
 */
public class CusLayoutUtil {

    public static int[] getPicSize(String res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(res, options); // 此时返回的bitmap为null
        return new int[]{options.outWidth, options.outHeight};
    }

}
