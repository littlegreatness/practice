package com.mcxtzhang.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.SysLoadAnimHelper;

public class Main2Activity extends AppCompatActivity {

    private PathAnimView pathAnimView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pathAnimView1 = (PathAnimView) findViewById(R.id.pathAnimView1);

        pathAnimView1.setSourcePath(null);
        //代码示例 动态对path加工，通过Helper
        pathAnimView1.setPathAnimHelper(new SysLoadAnimHelper(pathAnimView1, pathAnimView1.getSourcePath(), pathAnimView1.getAnimPath()));
        //设置颜色
        pathAnimView1.setColorBg(Color.WHITE).setColorFg(Color.YELLOW);
        //当然你可以自己拿到Paint，然后搞事情，我这里设置线条宽度
        pathAnimView1.getPaint().setStrokeWidth(20);


    }

    public void start(View view) {
        pathAnimView1.setAnimTime(1000).setAnimInfinite(true).startAnim();
    }

}
