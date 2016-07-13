package com.newtonker.jigsawdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.event.OnItemClickListener;
import com.newtonker.jigsawdemo.model.PopupFilterItem;

import java.util.List;

public class JigsawPopupAdapter extends RecyclerView.Adapter<JigsawPopupAdapter.ViewHolder>
{
    private int curSelection = 0;
    private LayoutInflater inflater;
    private List<PopupFilterItem> list;
    private OnItemClickListener onItemClickListener;

    public JigsawPopupAdapter(Context context, List<PopupFilterItem> list)
    {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.item_jigsaw_filter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        PopupFilterItem filterItem = list.get(position);
        holder.frameLayout.setBackgroundResource(filterItem.getFilterItem().getColorId());
        holder.imageView.setImageBitmap(filterItem.getBitmap());

        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                curSelection = position;
                notifyDataSetChanged();

                if(null != onItemClickListener)
                {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });

        if(curSelection == position)
        {
            holder.frameLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return null == list ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        private FrameLayout frameLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.item_jigsaw_filter_layout);
            imageView = (ImageView) itemView.findViewById(R.id.item_jigsaw_filter_image);
        }
    }

    public void setSelection(int position)
    {
        curSelection = position;
    }

    public void setList(List<PopupFilterItem> list)
    {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
}
