package com.prac.buxiaoqing.recyclerviewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private M2Adapter mAdapter;
    private final static int COUNT = 6;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new M2Adapter(this);
        mAdapter.setSize(2);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        Decoration decoration = new Decoration(this);
        mRecyclerView.addItemDecoration(decoration);

    }

    public void add(View view) {
        int count = mAdapter.getSize();
        if (count < COUNT)
            mAdapter.setSize(++count);
        else
            mAdapter.setSize(COUNT);

        mAdapter.notifyDataSetChanged();
    }

    public void minus(View view) {
        int count = mAdapter.getSize();
        if (count > 0)
            mAdapter.setSize(--count);
        else
            mAdapter.setSize(0);
        mAdapter.notifyDataSetChanged();
    }


    private static String[] getDatas() {
        String[] str = new String[COUNT];
        for (int i = 0; i < COUNT; i++) {
            if (i != COUNT - 1)
                str[i] = "I am No." + i + "  ->";
            else
                str[i] = "I am No." + i;
        }
        return str;
    }

    public static class M1Adapter extends RecyclerView.Adapter<M1Adapter.ViewHolder> {
        public String[] datas = null;

        public M1Adapter(String[] datas) {
            this.datas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(datas[position]);
        }

        @Override
        public int getItemCount() {
            return datas.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mTextView = (TextView) view.findViewById(R.id.text);
            }
        }
    }


    public static class M2Adapter extends RecyclerView.Adapter<M2Adapter.ViewHolder> {
        private Context mContext;
        private int size = 0;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }


        public M2Adapter(Context context) {
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.mRecyclerView.setLayoutManager(mLayoutManager);
            M1Adapter mAdapter = new M1Adapter(getDatas());
            viewHolder.mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public int getItemCount() {
            return size;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public RecyclerView mRecyclerView;

            public ViewHolder(View view) {
                super(view);
                mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            }
        }
    }
}
