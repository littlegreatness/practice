package com.prac.buxiaoqing.prac.gif;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.prac.buxiaoqing.prac.R;
import com.prac.buxiaoqing.prac.gif.view.AnimateView;


public class Animateact extends AppCompatActivity {

    private String TAG = "Animateact";
    private Button button;

    private boolean isDown;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            log("handleMessage");
        }
    };


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            log("runnable");
            handler.postDelayed(runnable, 300);
            play(button);
        }
    };

    private long time = System.currentTimeMillis();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_act);
        button = (Button) findViewById(R.id.button);


        button.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                isDown = true;
                                if (isDown) {
                                    handler.post(runnable);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                isDown = false;
                                break;
                        }
                        if (!isDown) {
                            handler.removeCallbacks(runnable);
                        }
                        return false;
                    }
                }
        );


    }


    public void play(final View view) {

        log("play");
//
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        // statusBarHeight是上面所求的状态栏的高度
//        int statusBarHeight = frame.top;
//
//        //正文内容开始的高度
//        int contentViewTop = getWindow()
//                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        // 获取标题栏高度
//        int titleBarHeight = contentViewTop - statusBarHeight;
//        Log.i("test", "statusBarHeight=" + statusBarHeight + " contentViewTop="
//                + contentViewTop + " titleBarHeight=" + titleBarHeight);
//
//        int width = view.getWidth();
//        int height = view.getHeight();

        //AnimateView animation = new AnimateView(getApplicationContext(), view.getX() + width / 2, view.getY() + height / 2 + contentViewTop, R.drawable.heart);
        AnimateView animation = new AnimateView(getApplicationContext(), 300, 650, R.drawable.heart);
        animation.startAnimation();
        ((ViewGroup) view.getRootView()).addView(animation);
    }


    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
