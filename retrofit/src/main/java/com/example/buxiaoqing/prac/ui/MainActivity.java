package com.example.buxiaoqing.prac.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.buxiaoqing.prac.R;


/**
 * Retrofit教程，分五篇，这里是Demo，我是夜闪冰，英文名是ysbing，希望大家学习愉快
 *
 * @author ysbing
 * @date 20160105
 */
public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        switch (id) {
            case R.id.button:
                intent = new Intent(this, Teach1Activity.class);
                break;
            case R.id.button2:
                intent = new Intent(this, Teach2Activity.class);
                break;
            case R.id.button3:
                intent = new Intent(this, Teach3Activity.class);
                break;
            case R.id.button4:
                intent = new Intent(this, Teach4Activity.class);
                break;
            case R.id.button5:
                intent = new Intent(this, Teach5Activity.class);
                break;

        }
        startActivity(intent);
    }
}
