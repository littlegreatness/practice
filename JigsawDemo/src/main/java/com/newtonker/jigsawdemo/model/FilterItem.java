package com.newtonker.jigsawdemo.model;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class FilterItem
{
    private GPUImageFilter filter;
    private int colorId;

    public FilterItem(GPUImageFilter filter, int colorId)
    {
        this.filter = filter;
        this.colorId = colorId;
    }

    public GPUImageFilter getFilter()
    {
        return filter;
    }

    public void setFilter(GPUImageFilter filter)
    {
        this.filter = filter;
    }

    public int getColorId()
    {
        return colorId;
    }

    public void setColorId(int colorId)
    {
        this.colorId = colorId;
    }
}
