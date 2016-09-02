package com.prac.buxiaoqing.prac.gif;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.view.ViewHelper;
import com.prac.buxiaoqing.prac.R;

public class SpringAct extends AppCompatActivity {

    private ImageView dragIv;
    SpringSystem springSys = SpringSystem.create();
    private Spring springX,springY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring);

        dragIv = (ImageView) findViewById(R.id.iv_drag);

        dragIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int key = event.getAction();

                switch(key){

                    case MotionEvent.ACTION_DOWN:
                        springX.setEndValue(1.0);
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        springX.setEndValue(0.0);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        springX = springSys
                .createSpring()
                .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(86, 7))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        float scale = 1f - (value * 0.1f);

                        //ViewHelper是nineoldandroids的一个动画兼容类
                        //可参阅：http://nineoldandroids.com/
                        ViewHelper.setScaleX(dragIv, scale);

                        ViewHelper.setScaleY(dragIv, scale);

                        ViewHelper.setTranslationX(dragIv ,value);
                        ViewHelper.setTranslationX(dragIv ,value);
                    }
                });
    }
}
