package com.prac.buxiaoqing.butterknifetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindArray(R.array.knife)
    String[] strings;
    @BindColor(R.color.colorAccent)
    int color1;
    @BindView(R.id.text1)
    TextView tv;
    //批量绑定
    @BindViews({R.id.text1, R.id.text2, R.id.text3})
    List<TextView> textViews;


    @BindColor(R.color.colorPrimaryDark)
    int color2;
    @BindColor(R.color.colorPrimary)
    int color3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //批量操作
        ButterKnife.apply(textViews, new ButterKnife.Action<TextView>() {
            @Override
            public void apply(@NonNull TextView view, int index) {
                switch (index) {
                    case 0:
                        view.setText(strings[0]);
                        view.setTextColor(color1);
                        break;
                    case 1:
                        view.setText(strings[1]);
                        view.setTextColor(color2);
                        break;
                    case 2:
                        view.setText(strings[2]);
                        view.setTextColor(color3);
                        break;
                }
            }
        });
    }


    //将多个id绑定到一个方法里
    @OnClick({R.id.text1, R.id.text2, R.id.text3})
    public void click(View view) {
        if (view.getId() == R.id.text1) {
            Toast.makeText(getApplicationContext(), "text1 cilck", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.text2) {
            Toast.makeText(getApplicationContext(), "text2 cilck", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.text3) {
            Toast.makeText(getApplicationContext(), "text3 cilck", Toast.LENGTH_SHORT).show();
        }
    }

    //将多个id绑定到一个方法里
    @OnClick(R.id.btn)
    public void anno(View view) {
        Intent it = new Intent(getApplicationContext(), myanno.class);
        startActivity(it);
    }


    //将多个id绑定到一个方法里
    @OnClick(R.id.btn1)
    public void hello(View view) {
        Intent it = new Intent(getApplicationContext(), Scalpel.class);
        startActivity(it);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
