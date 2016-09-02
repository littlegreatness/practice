package com.prac.buxiaoqing.meitudemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.prac.buxiaoqing.meitudemo.model.PicEntity;
import com.prac.buxiaoqing.meitudemo.util.CusLayoutUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author：buxiaoqing on 16/7/11 14:12
 * Just do IT(没有梦想,何必远方)
 */
public class CusNumLayout extends LinearLayout {
    private static final String TAG = CusNumLayout.class.getSimpleName();
    private Context context;

    private int curLine;//当前在第几行;

    private int curNum;//how many img in current line

    private int parentWidth;// parent width

    private int lineHeight;

    private HashMap<Integer, PicEntity> addDatas = new HashMap<>();

    private List<ImageView> imgList = new ArrayList<>();

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

    public void setParentSize(int parentWidth) {
        this.parentWidth = parentWidth;
        this.lineHeight = parentWidth;
    }

    public void setDatas(int posY) {
        this.curLine = posY;
        setDatas(addDatas, posY);
    }


    public void setDatas(final HashMap<Integer, PicEntity> datas, int posY) {
        this.addDatas = datas;
        this.curLine = posY;
        setCurNum(datas.size());
        log("run  posY =" + posY + "   datas.size() = " + datas.size());
        new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < datas.size(); i++) {
                    final PicEntity picEntity = datas.get(i);
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (picEntity.getResId() != null)
                        Glide.with(context).load(picEntity.getResId()).into(imageView);
                    int[] picSize = CusLayoutUtil.getPicSize(picEntity.getResId());
                    picEntity.setWidth(picSize[0]);
                    picEntity.setHeight(picSize[1]);

                    log("run  size i = " + i + " width = " + picSize[0] + " height = " + picSize[1]);
                    picEntity.setRate(picSize[1] / (picSize[0] * 1f));
                    int height = (int) (parentWidth / curNum * picEntity.getRate());
                    if (height < lineHeight)
                        lineHeight = height;

                    imageView.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            picEntity.setSelected(true);
                            return true;
                        }
                    });
                    imgList.add(imageView);
                }
            }
        }.run();
    }

    public int getCurNum() {
        return addDatas.size();
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


    public void buildView() {
        LayoutParams params = new LayoutParams(parentWidth, lineHeight);
        log("build view lineHeight = " + lineHeight);
        this.setLayoutParams(params);
        ViewGroup.LayoutParams picParams;
        for (int i = 0; i < curNum; i++) {
            PicEntity entity = addDatas.get(i);
            if (entity.getHeight() < lineHeight)
                lineHeight = entity.getHeight();
        }

        for (int i = 0; i < curNum; i++) {
            PicEntity entity = addDatas.get(i);
            int width = parentWidth / curNum;
            picParams = new ViewGroup.LayoutParams(width, lineHeight);
            Log.d(TAG, "width = " + i + "   " + width);
            Log.d(TAG, "height = " + i + "   " + lineHeight);
            imgList.get(i).setLayoutParams(picParams);
            this.addView(imgList.get(i));
        }
    }

    private Animation moveLeft, moveRight;


    private Animation getMoveLeft() {
        if (moveLeft == null) {
            moveLeft = new TranslateAnimation(0, -30, 0, 0);
            moveLeft.setDuration(200);
            moveLeft.setFillAfter(true);
            moveLeft.setRepeatMode(Animation.REVERSE);
            moveLeft.setRepeatCount(1);
            moveLeft.start();
        }
        return moveLeft;
    }

    private Animation getMoveRight() {
        if (moveRight == null) {
            moveRight = new TranslateAnimation(0, 30, 0, 0);
            moveRight.setDuration(200);
            moveRight.setFillAfter(true);
            moveRight.setRepeatMode(Animation.REVERSE);
            moveRight.setRepeatCount(1);
            moveRight.start();
        }
        return moveRight;
    }


    /**
     * 在图片移动的时候,左右移动,以显示当前要放置的位置的方法
     *
     * @param index
     */
    public void avoid(int index) {

        if (index > curNum)
            index = curNum;

        if (index == 0) {
            imgList.get(0).clearAnimation();

            imgList.get(0).setAnimation(getMoveRight());
            imgList.get(0).startAnimation(getMoveRight());

            log("move:00000000000");

        } else if (index == curNum) {
            imgList.get(curNum - 1).clearAnimation();

            imgList.get(curNum - 1).setAnimation(getMoveLeft());
            imgList.get(curNum - 1).startAnimation(getMoveLeft());
            log("move:33333333333");
        } else {
            imgList.get(index).clearAnimation();
            imgList.get(index).setAnimation(getMoveRight());
            imgList.get(index).startAnimation(getMoveRight());
            log("move:=================");
            imgList.get(index - 1).clearAnimation();
            imgList.get(index - 1).setAnimation(getMoveLeft());
            imgList.get(index - 1).startAnimation(getMoveLeft());
        }
    }


    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
