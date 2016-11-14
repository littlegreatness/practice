package com.prac.buxiaoqing.butterknifetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.anno.PrintAnno;
import com.prac.buxiaoqing.butterknifetest.poorknife.BindMyView;
import com.prac.buxiaoqing.butterknifetest.poorknife.PoorKnife;
import com.prac.buxiaoqing.myknifelibrary.MyKnife;


public class myanno extends AppCompatActivity {

    @BindMyView(R.id.text_1)
    TextView textView1;


    @Override
    @PrintAnno(10086)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_anno);

        PoorKnife.injectMyView(this);


        MyKnife.bindMyKnife(this);

        textView1.setText(" PoorKnife worked!!!");

        fun();
    }


    public void fun() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}