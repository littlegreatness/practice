package com.newtonker.jigsawdemo.utils;

import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.model.FilterItem;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;

/**
 * filter
 */
public class FilterUtils
{
    private static List<FilterItem> filterLists = new ArrayList<>();

    static
    {
        filterLists.add(new FilterItem(new GPUImageFilter(), R.color.filter_color0));
        filterLists.add(new FilterItem(new GPUImageSepiaFilter(), R.color.filter_color1));
        filterLists.add(new FilterItem(new GPUImageGrayscaleFilter(), R.color.filter_color2));
        filterLists.add(new FilterItem(new GPUImagePixelationFilter(), R.color.filter_color3));
        filterLists.add(new FilterItem(new GPUImageColorInvertFilter(), R.color.filter_color4));
        filterLists.add(new FilterItem(new GPUImageGaussianBlurFilter(), R.color.filter_color5));
        filterLists.add(new FilterItem(new GPUImageCrosshatchFilter(), R.color.filter_color6));
        filterLists.add(new FilterItem(new GPUImageDilationFilter(), R.color.filter_color7));
        filterLists.add(new FilterItem(new GPUImageToonFilter(), R.color.filter_color8));
    }

    public static List<FilterItem> getFilterLists()
    {
        return filterLists;
    }
}
