package com.newtonker.jigsawdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.dragGrideView.DragAdapter;
import com.newtonker.jigsawdemo.dragGrideView.DragGridView;

import java.util.ArrayList;
import java.util.List;

public class GridAct extends AppCompatActivity implements DragAdapter.changeListener {
    private ArrayList<String> selectedPaths;

    private DragGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        gridView = (DragGridView) findViewById(R.id.userGridView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        selectedPaths = intent.getStringArrayListExtra(SelectPhotoActivity.SELECTED_PATHS);
        gridView.setAdapter(new DragAdapter(this, selectedPaths));
    }

    @Override
    public void exchangeOtherAdapter(List<String> data, int position) {

    }

    @Override
    public void setCurrentPosition() {

    }
}
