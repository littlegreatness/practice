package com.prac.buxiaoqing.prac.gif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.prac.buxiaoqing.prac.R;
import com.prac.buxiaoqing.prac.gif.view.InnerScrollView;

public class InnerScrollViewAct extends AppCompatActivity {


    private ScrollView s1;
    private InnerScrollView s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_scroll_view);

        s1 = (ScrollView) findViewById(R.id.s1);
        s2 = (InnerScrollView) findViewById(R.id.s2);

        s2.setParentScrollView(s1);

    }
}
