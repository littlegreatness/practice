package com.newtonker.jigsawdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.dragView.DragLayout;
import com.newtonker.jigsawdemo.dragView.PicInfo;
import com.newtonker.jigsawdemo.dragView.RowInfo;
import com.newtonker.jigsawdemo.utils.Util;

import java.util.ArrayList;

public class DragAct extends AppCompatActivity {
    private ArrayList<String> selectedPaths;
    private ArrayList<PicInfo> pics;
    private DragLayout dragLayout;
    private PicInfo pic;
    private RowInfo rowInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        pic = (PicInfo) findViewById(R.id.pic);
        rowInfo = (RowInfo) findViewById(R.id.row);
        dragLayout = (DragLayout) findViewById(R.id.drag_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        selectedPaths = intent.getStringArrayListExtra(SelectPhotoActivity.SELECTED_PATHS);
        pics = new ArrayList<>();
        for (String str : selectedPaths) {
            PicInfo pic = new PicInfo(getApplicationContext());
            pic.setPicUrl(str);
            pics.add(pic);
        }
        pic.setPicUrl(selectedPaths.get(0));


        //rowInfo.setLists(600, pics.subList(0, 3));

        dragLayout.setImgPaths(pics);

    }
}
