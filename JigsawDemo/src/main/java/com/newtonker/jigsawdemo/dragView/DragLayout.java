package com.newtonker.jigsawdemo.dragView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.newtonker.jigsawdemo.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * author：buxiaoqing on 16/7/7 15:28
 * Just do IT(没有梦想,何必远方)
 */
public class DragLayout extends LinearLayout {

    private final String TAG = DragLayout.class.getSimpleName();

    private Context context;
    private static final int MAX_COUNT_LINE = 3;//   max count for single line
    private int curImgCount;//  img num    =  imgPaths.size()
    private int curRow;//       current row num   =   containerList.size()
    private int rootWidth, rootHeight;//DragLayout  width and height

    private List<PicInfo> imgPaths;// img url

    private HashMap<Integer, RowInfo> containerList;


    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rootWidth = getMeasuredWidth();
        rootHeight = getMeasuredHeight();
        Log.d(TAG, "rootWidth = " + rootWidth);
        Log.d(TAG, "rootHeight = " + rootHeight);
    }

    public void setImgPaths(List<PicInfo> imgPaths) {
        if (imgPaths == null || imgPaths.size() == 0)
            return;
        this.imgPaths = imgPaths;
        curImgCount = imgPaths.size();
        init();
    }

    //初始化的满布局
    private void init() {
        rootWidth = 656;
        rootHeight = 800;

        if (curImgCount % MAX_COUNT_LINE == 0)
            curRow = curImgCount / MAX_COUNT_LINE;
        else
            curRow = curImgCount / MAX_COUNT_LINE + 1;
        containerList = new HashMap<>();
        int curheight = 0;

        for (int i = 0; i < curRow; i++) {
            RowInfo rowInfo = new RowInfo(context);
            rowInfo.setRowPos(i);
            //3 imgs in single line
            if (i < curRow - 1) {
                rowInfo.setCurNum(MAX_COUNT_LINE);
                rowInfo.setLists(rootWidth, imgPaths.subList(i * 3, i * 3 + 3));
            }//last row would not be full
            else {
                rowInfo.setCurNum(curImgCount - i * MAX_COUNT_LINE);
                rowInfo.setLists(rootWidth, imgPaths.subList(i * MAX_COUNT_LINE, curImgCount));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    Util.px2dip(context, rowInfo.getCurHeight()));
            rowInfo.setLayoutParams(params);

            curheight = rowInfo.getCurHeight();
            Log.w("DragLayout", "curheight = " + curheight + "    " + Util.px2dip(context, curheight));
            Log.w("DragLayout", "rowInfo =   getChildCount = " + rowInfo.getChildCount() + "    getCurNum =" + rowInfo.getCurNum());
            this.addView(rowInfo);

            containerList.put(i, rowInfo);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(rootWidth,
                rootHeight);
        this.setLayoutParams(params);

        Log.w("DragLayout", "curheight = " + curheight + "    " + Util.px2dip(context, curheight));
        Log.w("DragLayout", "child count = " + getChildCount());
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {


    }
}
