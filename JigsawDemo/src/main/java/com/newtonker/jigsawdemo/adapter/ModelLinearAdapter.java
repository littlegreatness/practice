package com.newtonker.jigsawdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.event.OnModelItemClickListener;
import com.newtonker.jigsawdemo.widget.JigsawFrameLayout;
import com.newtonker.jigsawdemo.widget.JigsawModelLayout;

import java.util.List;

/**
 * Created by newtonker on 2015/12/29.
 */
public class ModelLinearAdapter extends RecyclerView.Adapter<ModelLinearAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<JigsawModelLayout> models;

    private OnModelItemClickListener onModelItemClickListener;

    public ModelLinearAdapter(Context context)
    {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.item_model, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        holder.mFrameLayout.removeAllViews();
        holder.mFrameLayout.addView(models.get(position));
        holder.mFrameLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(null != onModelItemClickListener)
                {
                    onModelItemClickListener.onClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return null == models ? 0 : models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private JigsawFrameLayout mFrameLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            mFrameLayout = (JigsawFrameLayout) itemView.findViewById(R.id.item_model_layout);
        }
    }

    public void setModels(List<JigsawModelLayout> models)
    {
        this.models = models;
    }

    public void setOnModelItemClickListener(OnModelItemClickListener onModelItemClickListener)
    {
        this.onModelItemClickListener = onModelItemClickListener;
    }
}
