package com.prac.buxiaoqing.butterknifetest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * author：buxiaoqing on 2016/11/11 11:56
 * Just do IT(没有梦想,何必远方)
 */
public class mScrollView extends ListView {

    public mScrollView(Context context) {
        this(context, null);
    }

    public mScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public mScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
