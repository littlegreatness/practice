package com.example.gif;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.gif.view.AnimateView;
import com.example.gif.view.FsgifView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, Integer> matchMap;
    private FsgifView gifView;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifView = (FsgifView) findViewById(R.id.gifView);
        et = (EditText) findViewById(R.id.et_content);
        initMap();
    }

    private void initMap() {
        matchMap = new HashMap<>();
        matchMap.put("想你了", R.drawable.heart);
        matchMap.put("恭喜发财", R.drawable.money);
        matchMap.put("嫁给我", R.drawable.ring);
        matchMap.put("该吃药了", R.drawable.pill);
    }

    public void playGif(View view) {
        if (gifView.isPaused())
            gifView.setPaused(false);
    }

    public void stopGif(View view) {
        if (!gifView.isPaused())
            gifView.setPaused(true);
    }

    public void clear(View view) {
        et.setText("");
    }

    public void play(View view) {
        int resId = 00;

        String content = et.getText().toString().trim();
        Set<String> strings = matchMap.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String keyword = iterator.next();
            if (content.contains(keyword)) {
                resId = matchMap.get(keyword);
            }
        }

        AnimateView animation = new AnimateView(getApplicationContext(), resId);
        animation.startAnimation();
        ((ViewGroup) view.getRootView()).addView(animation);
    }
}
