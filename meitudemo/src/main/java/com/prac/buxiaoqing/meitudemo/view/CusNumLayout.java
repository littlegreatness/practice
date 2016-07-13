package com.prac.buxiaoqing.meitudemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.prac.buxiaoqing.meitudemo.model.PicEntity;
import com.prac.buxiaoqing.meitudemo.util.CusLayoutUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author：buxiaoqing on 16/7/11 14:12
 * Just do IT(没有梦想,何必远方)
 */
public class CusNumLayout extends LinearLayout {
    private static final String TAG = CusNumLayout.class.getSimpleName();
    private Context context;

    private int curLine;//当前在第几行;

    private int curNum;//how many img in current line

    private int parentWidth, parentHeight;// parent width

    private int lineHeight;

    private ArrayList<PicEntity> datas;// size == curNum

    private HashMap<Integer, PicEntity> addDatas;

    public CusNumLayout(Context context) {
        this(context, null);
    }

    public CusNumLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusNumLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public void setParentSize(int parentWidth, int height) {
        this.parentWidth = parentWidth;
        this.parentHeight = height;
    }

    public void setDatas(int posY) {
        this.curLine = posY;
        setCurNum(datas.size());
        setDatas(datas, posY);
    }


    public void setDatas(final ArrayList<PicEntity> datas, int posY) {
        this.datas = datas;
        this.curLine = posY;
        setCurNum(datas.size());

        new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < datas.size(); i++) {
                    PicEntity picEntity = datas.get(i);
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(context).load(picEntity.getResId()).into(imageView);
//                    picEntity.setPosX(i);
//                    picEntity.setPosY(curLine);
                    int[] picSize = CusLayoutUtil.getPicSize(picEntity.getResId());
                    picEntity.setWidth(picSize[0]);
                    picEntity.setHeight(picSize[1]);
                    picEntity.setRate(picSize[1] / (picSize[0] * 1f));
                    picEntity.setImageView(imageView);
                }
            }
        }.run();
    }




    public int getCurNum() {
        return curNum;
    }

    public HashMap<Integer, PicEntity> getAddDatas() {
        if (addDatas == null)
            addDatas = new HashMap<>();
        return addDatas;
    }

    public void setAddDatas(HashMap<Integer, PicEntity> addDatas) {
        this.addDatas = addDatas;
    }

    public int getParentWidth() {
        return parentWidth;
    }

    public int getParentHeight() {
        return parentHeight;
    }

    public ArrayList<PicEntity> getDatas() {
        if (datas == null)
            datas = new ArrayList<>();
        return datas;
    }

    public void buildView() {
        LayoutParams params = new LayoutParams(parentWidth, parentHeight);
        this.setLayoutParams(params);
        ViewGroup.LayoutParams picParams;
        for (int i = 0; i < datas.size(); i++) {
            PicEntity entity = datas.get(i);
            if (entity.getHeight() < lineHeight)
                lineHeight = entity.getHeight();
        }

        for (int i = 0; i < datas.size(); i++) {
            PicEntity entity = datas.get(i);
            int width = parentWidth / curNum;
            picParams = new ViewGroup.LayoutParams(width, lineHeight);
            Log.d(TAG, "width = " + i + "   " + width);
            Log.d(TAG, "height = " + i + "   " + lineHeight);
            entity.getImageView().setLayoutParams(picParams);
            this.addView(entity.getImageView());
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
