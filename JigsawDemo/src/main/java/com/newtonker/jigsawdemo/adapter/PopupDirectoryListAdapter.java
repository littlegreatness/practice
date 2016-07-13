package com.newtonker.jigsawdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.model.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

public class PopupDirectoryListAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<PhotoDirectory> directories = new ArrayList<>();

    public PopupDirectoryListAdapter(Context context, List<PhotoDirectory> directories)
    {
        this.context = context;
        this.directories = directories;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return directories.size();
    }

    @Override
    public PhotoDirectory getItem(int position)
    {
        return directories.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return directories.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(directories.get(position));

        return convertView;
    }

    private class ViewHolder
    {
        public ImageView coverImage;
        public TextView dirName;
        public TextView dirNum;

        public ViewHolder(View rootView)
        {
            coverImage = (ImageView) rootView.findViewById(R.id.dir_cover_image);
            dirName = (TextView) rootView.findViewById(R.id.dir_name);
            dirNum = (TextView) rootView.findViewById(R.id.dir_num);
        }

        public void bindData(PhotoDirectory directory)
        {
            Glide.with(context).load(directory.getCoverPath()).thumbnail(0.1f).into(coverImage);
            dirName.setText(directory.getName());
            dirNum.setText(String.valueOf(directory.getPhotoNums()));
        }
    }
}
