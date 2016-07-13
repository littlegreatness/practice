package com.newtonker.jigsawdemo.dragView;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.List;

/**
 * author：buxiaoqing on 16/7/7 16:09
 * Just do IT(没有梦想,何必远方)
 */
public class RowInfo extends LinearLayout {
    private int curNum;//当前有几张图   = lists.size()
    private int rowPos;//位于第几行呢
    private int rootWidth;
    private int curWidth, curHeight;//该行里面图的尺寸信息,跟PicInfo里面的不一定一样
    private List<PicInfo> lists;//该行图片的资源,不超过3张

    public RowInfo(Context context) {
        this(context, null);
    }

    public RowInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getCurWidth() {
        return curWidth;
    }

    public int getCurHeight() {
        return curHeight;
    }

    public List<PicInfo> getLists() {
        return lists;
    }

    public void setLists(int rootWidth, List<PicInfo> lists) {
        if (lists != null && lists.size() > 0) {

            this.lists = lists;
            this.curNum = lists.size();
            this.curWidth = rootWidth / curNum;
            if (rootWidth > 0)
                this.curWidth = rootWidth / curNum;

            for (int i = 0; i < lists.size(); i++) {
                PicInfo pic = lists.get(i);
                int height = (int) (curWidth * pic.getRatio());
                Log.w("RowInfo", "rootWidth=" + rootWidth + "  pic.getRatio() = " + pic.getRatio() + "  height= " + height);
                if (curHeight == 0)
                    curHeight = height;
                else {
                    if (curHeight > height)
                        curHeight = height;
                }
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(curWidth, curHeight);
                pic.setLayoutParams(layoutParams);
                this.addView(pic);
                Log.w("RowInfo", "child count = " + getChildCount());
            }
        }
        Log.w("RowInfo", "rootWidth = " + rootWidth + "   curWidth = " + curWidth + "     curHeight = " + curHeight);

    }
}
