package com.prac.buxiaoqing.prac.gif;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.prac.buxiaoqing.prac.R;
import com.prac.buxiaoqing.prac.gif.view.AnimateView;

import java.io.File;

public class Animateact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_act);
    }


    public void play(View view) {


        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // statusBarHeight是上面所求的状态栏的高度
        int statusBarHeight = frame.top;

        //正文内容开始的高度
        int contentViewTop = getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // 获取标题栏高度
        int titleBarHeight = contentViewTop - statusBarHeight;
        Log.i("test", "statusBarHeight=" + statusBarHeight + " contentViewTop="
                + contentViewTop + " titleBarHeight=" + titleBarHeight);


        int width = view.getWidth();
        int height = view.getHeight();

        AnimateView animation = new AnimateView(this, view.getX() + width / 2, view.getY() + height / 2 + contentViewTop, R.drawable.heart);
        animation.startAnimation();
        ((ViewGroup) view.getRootView()).addView(animation);
    }
}
