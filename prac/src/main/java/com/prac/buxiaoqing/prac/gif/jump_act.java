package com.prac.buxiaoqing.prac.gif;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;

import com.prac.buxiaoqing.prac.R;

public class jump_act extends AppCompatActivity {

    Button back;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_act);


        back = (Button) findViewById(R.id.back);

        Fade slide = new Fade();
        slide.setDuration(1000);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setEnterTransition(slide);

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, back, "jump");
        startActivity(intent, activityOptionsCompat.toBundle());
    }
}
