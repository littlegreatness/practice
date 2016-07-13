package com.newtonker.jigsawdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.event.OnItemCheckListener;
import com.newtonker.jigsawdemo.event.OnPhotoCheckedChangeListener;
import com.newtonker.jigsawdemo.model.Photo;
import com.newtonker.jigsawdemo.model.PhotoDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder>
{
    private Context mContext;
    private LayoutInflater inflater;

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoCheckedChangeListener onPhotoCheckedChangeListener = null;

    public PhotoGridAdapter(Context mContext, List<PhotoDirectory> photoDirectories)
    {
        this.mContext = mContext;
        this.photoDirectories = photoDirectories;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position)
    {
        if (mContext instanceof Activity && ((Activity) mContext).isDestroyed())
        {
            return;
        }

        List<Photo> photos = getCurrentPhotos();
        final Photo photo = photos.get(position);

        Glide.with(mContext).load(new File(photo.getPath())).centerCrop().thumbnail(0.1f).placeholder(R.mipmap.ic_photo_black_48dp).error(R.mipmap.ic_broken_image_black_48dp).into(holder.ivPhoto);

        final boolean isChecked = isSelected(photo);

        holder.vSelected.setSelected(isChecked);
        holder.ivPhoto.setSelected(isChecked);

        holder.vSelected.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean isEnable = true;

                if (onItemCheckListener != null)
                {
                    isEnable = onItemCheckListener.OnItemCheck(position, photo, isChecked, getSelectedPhotos().size());
                }
                if (isEnable)
                {
                    toggleSelection(photo);
                    notifyItemChanged(position);
                }
                if (null != onPhotoCheckedChangeListener)
                {
                    onPhotoCheckedChangeListener.onCheckedChange(getSelectedPhotoPaths());
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return 0 == photoDirectories.size() ? 0 : getCurrentPhotos().size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView)
        {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
        }
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener)
    {
        this.onItemCheckListener = onItemCheckListener;
    }

    public void setOnPhotoCheckedChangeListener(OnPhotoCheckedChangeListener onPhotoCheckedChangeListener)
    {
        this.onPhotoCheckedChangeListener = onPhotoCheckedChangeListener;
    }

    public ArrayList<String> getSelectedPhotoPaths()
    {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());

        for (Photo photo : selectedPhotos)
        {
            selectedPhotoPaths.add(photo.getPath());
        }

        return selectedPhotoPaths;
    }
}
