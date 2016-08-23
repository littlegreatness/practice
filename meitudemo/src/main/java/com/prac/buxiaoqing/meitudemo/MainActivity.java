package com.prac.buxiaoqing.meitudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prac.buxiaoqing.meitudemo.view.CusNumLayout;
import com.prac.buxiaoqing.meitudemo.view.CusPicLayout;
import com.prac.buxiaoqing.meitudemo.view.CustomLabelLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private CustomLabelLayout customLabelLayout;
    private CusNumLayout line;
    private CusPicLayout pic_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<String> pics = new ArrayList<>();
        pics.add("/storage/emulated/0/sina/weibo/weibo/img-84c5d2f84a55ffcbaaa359dd7e4d6d97.jpg");
        pics.add("/storage/emulated/0/sina/weibo/weibo/img-4ffc29ee7f651e715ec4d2007237e2e6.jpg");
        pics.add("/storage/emulated/0/news_article/a75fe8cd324d12289201113b67dea41f.jpg");
        pics.add("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
        pics.add("/storage/emulated/0/sina/weibo/weibo/img-84c5d2f84a55ffcbaaa359dd7e4d6d97.jpg");
        pics.add("/storage/emulated/0/sina/weibo/weibo/img-84c5d2f84a55ffcbaaa359dd7e4d6d97.jpg");
        pics.add("/storage/emulated/0/sina/weibo/weibo/img-84c5d2f84a55ffcbaaa359dd7e4d6d97.jpg");
        pics.add("/storage/emulated/0/sina/weibo/weibo/img-84c5d2f84a55ffcbaaa359dd7e4d6d97.jpg");

        pic_layout = (CusPicLayout) findViewById(R.id.pic_layout);

        pic_layout.generateEnties(pics);

        pic_layout.initView();

    }


}
