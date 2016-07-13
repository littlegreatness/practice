package com.newtonker.jigsawdemo.event;

import android.view.View;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public interface OnFilterItemClickListener
{
    void onItemClick(View view, int position, GPUImageFilter filter);
}
