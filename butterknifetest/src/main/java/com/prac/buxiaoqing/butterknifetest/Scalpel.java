package com.prac.buxiaoqing.butterknifetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.jakewharton.scalpel.ScalpelFrameLayout;

public class Scalpel extends AppCompatActivity {

    private ScalpelFrameLayout sf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sf = (ScalpelFrameLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main2, null);
        setContentView(sf);

        sf.setLayerInteractionEnabled(true);
    }
}
