package com.cml.newframe.scrollablebox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private GridView gridview;

    private String[] strs = new String[]{"content", "content", "content", "content",
            "content", "content", "content", "content", "content", "content", "content",
            "content", "content", "content", "content", "content", "content", "content",
            "content", "content", "content", "content", "content", "content", "content",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return strs.length;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);
                }
                ((TextView)view.findViewById(R.id.tv)).setText(strs[i]);

                return view;
            }
        });
    }

    public void click(View v) {
        Toast.makeText(this, "点击：" + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
    }
}
