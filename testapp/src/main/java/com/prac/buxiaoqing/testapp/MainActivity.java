package com.prac.buxiaoqing.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);

        List<SearchItem> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new SearchItem("item:" + i));
        }

        mAdapter adapter = new mAdapter();
        adapter.setList(list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    public void sayHello(View v) {
        TextView textView = (TextView) findViewById(R.id.textView);
        EditText editText = (EditText) findViewById(R.id.editText);
        textView.setText("Hello,Peter!");//+ editText.getText().toString() + "!"
    }

    public class SearchItem {
        private String keyword;

        public SearchItem(String keyword) {
            this.keyword = keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }
    }


    class mAdapter extends BaseAdapter {
        public void setList(List<SearchItem> list) {
            this.list = list;
        }

        private List<SearchItem> list;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_list, null);
            ((TextView) view.findViewById(R.id.textView_item)).setText(list.get(i).getKeyword());
            return view;
        }
    }


}
