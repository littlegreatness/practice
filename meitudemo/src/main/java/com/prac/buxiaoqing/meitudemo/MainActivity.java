package com.prac.buxiaoqing.meitudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prac.buxiaoqing.meitudemo.model.PicEntity;
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

        customLabelLayout = (CustomLabelLayout) findViewById(R.id.cus_layout);

        customLabelLayout.setMaxLines(10);
        customLabelLayout.setAllowLongClick(true);
        customLabelLayout.setIsAllowScroll(true);
//         customLabelLayout.changeThemeForTextColor(this.getResources().getColor(R.color.blackzi), 
//        this.getResources().getColor(R.color.blackzi), 
//        R.drawable.cus_label_round_corner_stroke_bg_normal, 
//        R.drawable.cus_label_round_corner_stroke_bg_normal); 
        //customLabelLayout.addAllBody(this.getResources().getStringArray(R.array.cities));
        customLabelLayout.addAllBody(new String[]{"北京市", "天津市", "重庆市", "上海市", "济南市", "长沙市", "武汉市", "南京市", "西安市", "高雄市",
                "哈尔滨市", "荆州市", "南宁市", "兰州市", "呼和浩特市", "吉林市", "沈阳市"});


        line = (CusNumLayout) findViewById(R.id.line);
        line.setParentSize(650);

        ArrayList<PicEntity> list = new ArrayList<>();
        PicEntity picEntity0 = new PicEntity("/storage/emulated/0/sina/weibo/weibo/img-84c5d2f84a55ffcbaaa359dd7e4d6d97.jpg");
        picEntity0.setPosX(0);
        picEntity0.setPosY(0);
        list.add(0, picEntity0);

        PicEntity picEntity1 = new PicEntity("/storage/emulated/0/sina/weibo/weibo/img-4ffc29ee7f651e715ec4d2007237e2e6.jpg");
        picEntity1.setPosX(1);
        picEntity1.setPosY(0);
        list.add(1, picEntity1);

        PicEntity picEntity2 = new PicEntity("/storage/emulated/0/news_article/a75fe8cd324d12289201113b67dea41f.jpg");
        picEntity2.setPosX(2);
        picEntity2.setPosY(0);
        list.add(2, picEntity2);

        PicEntity picEntity3 = new PicEntity("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
        picEntity3.setPosX(0);
        picEntity3.setPosY(1);
        list.add(3, picEntity3);

        PicEntity picEntity4 = new PicEntity("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
        picEntity4.setPosX(1);
        picEntity4.setPosY(1);
        list.add(4, picEntity4);

        PicEntity picEntity5 = new PicEntity("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
        picEntity5.setPosX(2);
        picEntity5.setPosY(1);
        list.add(5, picEntity5);

        PicEntity picEntity6 = new PicEntity("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
        picEntity6.setPosX(0);
        picEntity6.setPosY(2);
        list.add(6, picEntity6);

        PicEntity picEntity7 = new PicEntity("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
        picEntity7.setPosX(1);
        picEntity7.setPosY(2);
        list.add(7, picEntity7);

//        PicEntity picEntity8 = new PicEntity("/storage/emulated/0/tencent/MicroMsg/c15693f514f1eb28f6aecdc6d6eb7bdatemp1458386595021/sfs/avatar/d4/52/user_hd_d45241532519c6e5c16b30acd247f77b.png");
//        picEntity8.setPosX(0);
//        picEntity8.setPosY(4);
//        list.add(8, picEntity8);

//      line.setDatas(list, 0);
//      line.buildView();

        pic_layout = (CusPicLayout) findViewById(R.id.pic_layout);

        pic_layout.setPicEntities(list);

        pic_layout.initView();


    }
}
