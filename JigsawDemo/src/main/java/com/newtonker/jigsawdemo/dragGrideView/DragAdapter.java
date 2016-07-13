package com.newtonker.jigsawdemo.dragGrideView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.newtonker.jigsawdemo.R;

import java.util.List;

/**
 * Created by guolei on 16-3-14.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * |        没有神兽，风骚依旧！          |
 * |        QQ:1120832563             |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */

public class DragAdapter extends BaseDragAdapter {

    private static final String TAG = "DragAdapter";

    private Context context;
    private int dropPosition = -1;
    private List<String> provinceList;
    private String selectItem;

    public DragAdapter(Context context, List<String> provinceList) {
        this.context = context;
        this.provinceList = provinceList;
        selectItem = provinceList.get(0);
    }

    @Override
    public int getCount() {
        return provinceList == null ? 0 : provinceList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null != provinceList && provinceList.size() != 0) {
            return provinceList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: 16-3-26 控件的ｂｕｇ 不能使用convertView and holder
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);

        final String item = provinceList.get(position);
        Glide.with(context).load(item).asBitmap().into(imageView);
        if (dropPosition == position) {
            view.setVisibility(View.GONE);
        }
        if (selectItem == provinceList.get(position)) {
            view.setBackgroundColor(Color.parseColor("#fbfbfb"));
        } else {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return view;
//        ViewHolder holder ;
//        if (null == convertView){
//            convertView = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
//            holder = new ViewHolder();
//            holder.textView = (TextView) convertView.findViewById(R.id.title);
//            convertView.setTag(holder);
//        }else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        final ProvinceItem item = provinceList.get(position);
//        holder.textView.setText(item.getName()+"");
//        if (dropPosition == position){
//            convertView.setVisibility(View.INVISIBLE);
//        }
//        if (selectItem.getId() == provinceList.get(position).getId()){
//            convertView.setBackgroundColor(Color.parseColor("#fbfbfb"));
//            holder.textView.setTextColor(Color.parseColor("#ff604f"));
//        }else {
//            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.textView.setTextColor(Color.parseColor("#464646"));
//        }
//
//        Log.e(TAG, "getView: " + position + "dropPosition " + dropPosition + "visiable :"+convertView.getVisibility());
//        return convertView;
    }

    @Override
    public void addItem(String item) {
        provinceList.add((String) item);
        notifyDataSetChanged();
    }

    @Override
    public void exchange(int dragPosition, int dropPosition) {
        // TODO: 16-3-22 互换位置
        this.dropPosition = dropPosition;
        String dragItem = (String) getItem(dragPosition);
        if (dragPosition < dropPosition) {
            provinceList.add(dropPosition + 1, dragItem);
            provinceList.remove(dragPosition);
        } else {
            provinceList.add(dropPosition, dragItem);
            provinceList.remove(dragPosition + 1);
        }
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(String item) {
        if (provinceList.contains((String) item)) {
            provinceList.remove((String) item);
            dropPosition = -1;
            notifyDataSetChanged();
        }
    }

    @Override
    public void removePosition(int position) {
        if (position >= 0 && position < provinceList.size()) {
            provinceList.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public void dragEnd() {
        // TODO: 16-3-26 拖动完成的回调
        int position = 0;
        for (int i = 0; i < provinceList.size(); i++) {
            if (selectItem == provinceList.get(i)) {
                position = i;
                break;
            }
        }

        this.dropPosition = -1;
        if (null != listener) {
            listener.exchangeOtherAdapter(provinceList, position);
        }
        notifyDataSetChanged();
    }

    private changeListener listener;

    public void setListener(changeListener listener) {
        this.listener = listener;
    }

    public interface changeListener {

        public void exchangeOtherAdapter(List<String> data, int position);

        public void setCurrentPosition();
    }

    private class ViewHolder {
        private View view;
        private TextView textView;
    }

}
