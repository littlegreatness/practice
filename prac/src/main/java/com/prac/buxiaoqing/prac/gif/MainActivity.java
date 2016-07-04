package com.prac.buxiaoqing.prac.gif;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.prac.buxiaoqing.prac.R;
import com.prac.buxiaoqing.prac.gif.view.AnimateView;
import com.prac.buxiaoqing.prac.gif.view.DragLayout;
import com.prac.buxiaoqing.prac.gif.view.FsgifView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private HashMap<String, Integer> matchMap;
    FsgifView gifView;
    EditText et;
    DragLayout rootView;
    Button shareView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        shareView = (Button) findViewById(R.id.jump);
        et = (EditText) findViewById(R.id.et_content);
        rootView = (DragLayout) findViewById(R.id.rootView);
        gifView = (FsgifView) findViewById(R.id.gifView);


        Fade slide = new Fade();
        slide.setDuration(1000);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setExitTransition(slide);


        rootView.setDragListener(new DragLayout.DragListener() {
            @Override
            public void open() {

            }

            @Override
            public void close() {

            }

            @Override
            public void drag(float percent) {
                Log.d(TAG, " percent= " + percent);
            }
        });
        initMap();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initMap() {
        matchMap = new HashMap<>();
        matchMap.put("想你了", R.drawable.heart);
        matchMap.put("恭喜发财", R.drawable.money);
        matchMap.put("嫁给我", R.drawable.ring);
        matchMap.put("该吃药了", R.drawable.pill);
        matchMap.put("miss", R.drawable.heart);
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

        AnimateView animation = new AnimateView(this, resId);
        animation.startAnimation();
        ((ViewGroup) view.getRootView()).addView(animation);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void jump(View view) {
        Intent intent = new Intent(this, jump_act.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, shareView, "jump");
        startActivity(intent, activityOptionsCompat.toBundle());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.prac.buxiaoqing.prac.gif/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.prac.buxiaoqing.prac.gif/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}