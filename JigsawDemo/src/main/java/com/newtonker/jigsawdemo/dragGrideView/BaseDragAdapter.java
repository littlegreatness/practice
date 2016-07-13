package com.newtonker.jigsawdemo.dragGrideView;

import android.widget.BaseAdapter;

/**
 * Created by guolei on 16-3-14.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * |        没有神兽，风骚依旧！          |
 * |        QQ:1120832563             |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */

public abstract class BaseDragAdapter extends BaseAdapter{

    public abstract void addItem(String item);
    public abstract void exchange(int dragPosition,int dropPosition);
    public abstract void removeItem(String item);
    public abstract void removePosition(int position);
    public abstract void dragEnd();
}