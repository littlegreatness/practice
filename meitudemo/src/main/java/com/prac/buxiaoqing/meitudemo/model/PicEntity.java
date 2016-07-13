package com.prac.buxiaoqing.meitudemo.model;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * author：buxiaoqing on 16/7/11 14:07
 * Just do IT(没有梦想,何必远方)
 */
public class PicEntity implements Serializable {

    public PicEntity(String resId) {
        this.resId = resId;
    }

    private ImageView imageView;

    private String resId;

    private boolean selected;

    private int posX;//当前所在的位置编号   第几行  第几个

    private int posY;

    private int width, height;

    private float rate;// height/width

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;

        if (imageView != null)
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selected = true;
                    return true;
                }
            });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


}
