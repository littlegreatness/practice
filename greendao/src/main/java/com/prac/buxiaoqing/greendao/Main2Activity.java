package com.prac.buxiaoqing.greendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prac.buxiaoqing.greendao.glide.MyTransformation;

public class Main2Activity extends AppCompatActivity {

    private ImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ivPic = (ImageView) findViewById(R.id.iv_pic);

        String url = "http://p.cdn.sohu.com/bc4e413e/80c76a9bf132875731f58bb23d9194b0.jpeg";
        loadPic(url);
    }

    private void loadPic(String url) {
        Glide.with(this).load(url).thumbnail(0.8f).override(800, 600).centerCrop().
                placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).skipMemoryCache(true)
                .priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivPic);
    }
}
