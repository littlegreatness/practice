package com.prac.buxiaoqing.prac.gif.model;

import android.graphics.Path;
import android.graphics.drawable.Drawable;

import dagger.Module;
import dagger.Provides;

public class KeyWordAnimationNode {

    private int resId;         // 资源ID
    private float x;           // x轴坐标
    private float y;           // y轴坐标
    private Path path;         // 运行轨迹
    private float value;       //
    private float speedY;
    private float speedX;
    private Drawable drawable;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    @Override
    public String toString() {
        return "Fllower [resId=" + resId + ", x=" + x + ", y=" + y + ", path="
                + path + ", value=" + value + "]";
    }
}
