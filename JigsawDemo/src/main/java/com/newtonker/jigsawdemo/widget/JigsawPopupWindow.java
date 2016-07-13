package com.newtonker.jigsawdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;


import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.adapter.JigsawPopupAdapter;
import com.newtonker.jigsawdemo.event.OnFilterItemClickListener;
import com.newtonker.jigsawdemo.event.OnItemClickListener;
import com.newtonker.jigsawdemo.model.FilterItem;
import com.newtonker.jigsawdemo.utils.DisplayUtils;
import com.newtonker.jigsawdemo.utils.FilterUtils;
import com.newtonker.jigsawdemo.model.PopupFilterItem;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class JigsawPopupWindow extends PopupWindow
{
    private static final int ITEM_PS_FILTER_WIDTH_IN_DP = 60;

    private Context context;

    private Bitmap originBitmap;

    private ImageView selectBtn;
    private RecyclerView recyclerView;

    private List<FilterItem> filterList;
    private List<PopupFilterItem> popupFilterList;

    private JigsawPopupAdapter popupAdapter;

    public JigsawPopupWindow(Context context)
    {
        super(context);

        this.context = context;

        View contentView = LayoutInflater.from(context).inflate(R.layout.jiagsaw_popup_window_layout, null);

        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(false);
        // 设置是否有悬浮效果
//        setBackgroundDrawable(new BitmapDrawable());
        // 设置是否点击PopupWindow外退出PopupWindow，启用后，图片选中效果无法绘制
//        setOutsideTouchable(true);

        init(contentView);
    }

    private void init(View contentView)
    {
        selectBtn = (ImageView) contentView.findViewById(R.id.jigsaw_select_btn);

        recyclerView = (RecyclerView) contentView.findViewById(R.id.jigsaw_filter_horizontal_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        // 先获取一个list组成需要的滤镜列表
        filterList = FilterUtils.getFilterLists();
        popupFilterList = new ArrayList<>();

        //先获取到一个bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_popup_origin, options);

        for(FilterItem filterItem : filterList)
        {
            popupFilterList.add(new PopupFilterItem(filterItem, originBitmap));
        }

        popupAdapter = new JigsawPopupAdapter(context, popupFilterList);

        recyclerView.setAdapter(popupAdapter);

        new ImageRenderTask(filterList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    /**
     * 设置当前选中的滤镜
     * @param position
     */
    public void setSelectedFilterPosition(int position)
    {
        popupAdapter.setSelection(position);
        popupAdapter.notifyDataSetChanged();
        // 滑动到选中的位置
        scrollToCenter(position);
    }

    public void scrollToCenter(int position)
    {
        int itemWidth = DisplayUtils.dp2px(context, ITEM_PS_FILTER_WIDTH_IN_DP);
        // 减掉一个选图占用的位置
        int middleX = (DisplayUtils.getScreenWidth(context) - itemWidth) / 2;
        int offset = recyclerView.computeHorizontalScrollOffset();
        int selectedOffset = itemWidth * position + itemWidth / 2;
        int x = selectedOffset - offset - middleX;

        recyclerView.smoothScrollBy(x, 0);
    }

    /**
     * 选图按钮侦听
     * @param onClickListener
     */
    public void setSelectListener(final View.OnClickListener onClickListener)
    {
        selectBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClickListener)
                {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    /**
     * 滤镜列表侦听
     * @param onFilterItemClickListener
     */
    public void setOnFilterItemClickListener(final OnFilterItemClickListener onFilterItemClickListener)
    {
        popupAdapter.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                FilterItem filterItem = filterList.get(position);
                GPUImageFilter filter = filterItem.getFilter();

                if (null != onFilterItemClickListener)
                {
                    onFilterItemClickListener.onItemClick(view, position, filter);
                }

                // 滑动到中间位置
                scrollToCenter(position);
            }
        });
    }

    // task
    private class ImageRenderTask extends AsyncTask<Void, Void, List<PopupFilterItem>>
    {
        private GPUImage gpuImage;
        private List<FilterItem> filterList;

        public ImageRenderTask(List<FilterItem> filterList)
        {
            this.filterList = filterList;
        }

        @Override
        protected void onPreExecute()
        {
            gpuImage = new GPUImage(context);
        }

        @Override
        protected List<PopupFilterItem> doInBackground(Void... params)
        {
            List<PopupFilterItem> popupFilterList = new ArrayList<>();
            int size = filterList.size();

            for (int i = 0; i < size; i++)
            {
                FilterItem filterItem = filterList.get(i);
                gpuImage.setFilter(filterItem.getFilter());
                Bitmap pvBitmap = gpuImage.getBitmapWithFilterApplied(originBitmap);

                //封装对象
                PopupFilterItem item = new PopupFilterItem();
                item.setBitmap(pvBitmap);
                item.setFilterItem(filterList.get(i));

                popupFilterList.add(item);
            }

            return popupFilterList;
        }

        @Override
        protected void onPostExecute(List<PopupFilterItem> result)
        {
            popupFilterList.clear();
            popupFilterList.addAll(result);
            popupAdapter.notifyDataSetChanged();
        }
    }
}
