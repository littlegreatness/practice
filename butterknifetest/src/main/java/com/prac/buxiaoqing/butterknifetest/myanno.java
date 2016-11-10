package com.prac.buxiaoqing.butterknifetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.prac.buxiaoqing.butterknifetest.poorknife.BindMyView;
import com.prac.buxiaoqing.butterknifetest.poorknife.PoorKnife;

import main.java.com.anno.PrintMe;


public class myanno extends AppCompatActivity {


    @BindMyView(R.id.text1)
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_anno);

        PoorKnife.injectMyView(this);
        textView1.setText(" PoorKnife worked!!!");
    }

    @PrintMe
    public void fun() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}