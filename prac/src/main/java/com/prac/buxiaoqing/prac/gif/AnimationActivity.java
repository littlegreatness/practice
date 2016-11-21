package com.prac.buxiaoqing.prac.gif;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.prac.buxiaoqing.prac.R;

public class AnimationActivity extends AppCompatActivity {
    private String tag = "AnimationActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        textView = (TextView) findViewById(R.id.text);


//        ObjectAnimator animator = new ObjectAnimator().ofFloat(textView, "translationX", 0.0f, 500f);
//        animator.setDuration(2000);
//        animator.setRepeatCount(-1);
//        animator.setRepeatMode(ValueAnimator.RESTART);
//        animator.setStartDelay(3000);
//        animator.start();
////
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//
//
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//            }
//        });
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                animation.getAnimatedValue();
//                animation.getCurrentPlayTime();
//                animation.getDuration();
//                animation.getStartDelay();
//                //...
//            }
//        });
//        AnimatorSet set = new AnimatorSet();

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationX", 0.0f, 500f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(textView, "rotationX", 0f, 45f, 90f, 45f);
        set.play(animator).with(animator1);
        set.start();

        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("translationX", 0.0f, 300.0f);
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.5f);
        PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat("rotationX", 0.0f, 90.0f, 0.0F);
        PropertyValuesHolder valuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.3f, 1.0F);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(textView, valuesHolder, valuesHolder1, valuesHolder2, valuesHolder3);
        objectAnimator.setDuration(2000).start();


        Keyframe kf1 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf2 = Keyframe.ofFloat(0.5f, 200f);
        Keyframe kf3 = Keyframe.ofFloat(1f, 600f);

        PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe("translationX", kf1, kf2, kf3);
        ObjectAnimator ob = ObjectAnimator.ofPropertyValuesHolder(textView, pvh);
        ob.start();
    }

    private void log(String str) {
        Log.e(tag, str);
    }

}
