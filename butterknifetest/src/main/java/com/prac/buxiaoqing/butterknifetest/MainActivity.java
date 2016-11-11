package com.prac.buxiaoqing.butterknifetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private SharedPreferences sp;
    @BindView(R.id.et)
    EditText et;

    @BindView(R.id.listView)
    mScrollView listView;
    private mViewGroup mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mView = (mViewGroup) findViewById(R.id.mView);

        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        });

        sp.edit().putString("str", " original ...").apply();

        //防抖操作   2秒只取一次点击事件
        RxView.clicks(textViews.get(0)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(getApplicationContext(), "text1 cilck", Toast.LENGTH_SHORT).show();
            }
        });

        RxView.longClicks(textViews.get(1)).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(getApplicationContext(), "text2 long click", Toast.LENGTH_SHORT).show();
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        listView.setAdapter(adapter);

        RxTextView.textChanges(et).debounce(500, TimeUnit.MILLISECONDS)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        //get the keyword
                        String key = charSequence.toString();
                        Log.e("CharSequence", " key =" + key);
                        return key;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String key) {
                        List<String> dataList = new ArrayList<String>();

                        if (!TextUtils.isEmpty(key)) {
                            for (String s : getData()) {
                                if (s != null) {
                                    if (s.contains(key)) {
                                        dataList.add(s);
                                        Log.e("dataList", "size changed =" + dataList.size());
                                    }
                                }
                            }
                        }
                        return dataList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings1 -> {
                    adapter.clear();
                    adapter.addAll(strings1);
                    adapter.notifyDataSetChanged();
                });

        //批量操作
        ButterKnife.apply(textViews, new ButterKnife.Action<TextView>()

                {
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
                }

        );
    }

    private String[] getData() {
        String[] str = new String[10];

        for (int i = 0; i < 10; i++) {
            str[i] = "abc" + i;
        }
        return str;

    }


    //将多个id绑定到一个方法里
    @OnClick({R.id.text1, R.id.text2, R.id.text3})
    public void click(View view) {
        if (view.getId() == R.id.text1) {
            //Toast.makeText(getApplicationContext(), "text1 cilck", Toast.LENGTH_SHORT).show();
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
