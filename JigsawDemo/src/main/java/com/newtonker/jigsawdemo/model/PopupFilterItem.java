package com.newtonker.jigsawdemo.model;

import android.graphics.Bitmap;

import com.newtonker.jigsawdemo.model.FilterItem;

public class PopupFilterItem
{
    private FilterItem filterItem;
    private Bitmap bitmap;

    public PopupFilterItem()
    {
    }

    public PopupFilterItem(FilterItem filterItem, Bitmap bitmap)
    {
        this.filterItem = filterItem;
        this.bitmap = bitmap;
    }

    public FilterItem getFilterItem()
    {
        return filterItem;
    }

    public void setFilterItem(FilterItem filterItem)
    {
        this.filterItem = filterItem;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
}
