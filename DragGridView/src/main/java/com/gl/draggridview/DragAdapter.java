package com.gl.draggridview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gl.base.BaseDragAdapter;
import com.gl.base.BaseItem;
import com.gl.bean.ProvinceItem;
import com.gl.tools.Constant;
import com.gl.tools.ListToJson;

import java.util.List;

/**
 * Created by guolei on 16-3-14.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * |        没有神兽，风骚依旧！          |
 * |        QQ:1120832563             |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * <p/>
 * 拖动只是改变了数据的位置而已
 */

public class DragAdapter extends BaseDragAdapter {

    private static final String TAG = "DragAdapter";

    private Context context;
    private int dropPosition = -1;
    private List<ProvinceItem> provinceList;
    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;
    private ProvinceItem selectItem;

    public DragAdapter(Context context, List<ProvinceItem> provinceList) {
        this.context = context;
        this.provinceList = provinceList;
        mShared = context.getSharedPreferences(Constant.USER, 0);
        mEditor = mShared.edit();
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
        TextView textView = (TextView) view.findViewById(R.id.title);

        final ProvinceItem item = provinceList.get(position);
        textView.setText(item.getName() + "");
        if (dropPosition == position) {
            view.setVisibility(View.GONE);
        }
        if (selectItem.getId() == provinceList.get(position).getId()) {
            view.setBackgroundColor(Color.parseColor("#fbfbfb"));
            textView.setTextColor(Color.parseColor("#ff604f"));
        } else {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            textView.setTextColor(Color.parseColor("#464646"));
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
    public void addItem(BaseItem item) {
        provinceList.add((ProvinceItem) item);
        notifyDataSetChanged();
    }

    @Override
    public void exchange(int dragPosition, int dropPosition) {
        // TODO: 16-3-22 互换位置
        this.dropPosition = dropPosition;
        ProvinceItem dragItem = (ProvinceItem) getItem(dragPosition);
        //往后拖
        if (dragPosition < dropPosition) {
            provinceList.add(dropPosition + 1, dragItem);
            provinceList.remove(dragPosition);
        }//往前拖
        else {
            provinceList.add(dropPosition, dragItem);
            provinceList.remove(dragPosition + 1);
        }
        mEditor.putString(Constant.PROVINCE, ListToJson.toJson(provinceList).toString());
        mEditor.commit();
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(BaseItem item) {
        if (provinceList.contains((ProvinceItem) item)) {
            provinceList.remove((ProvinceItem) item);
            dropPosition = -1;
            notifyDataSetChanged();
        }
    }

    @Override
    public void removePosition(int position) {
        if (position >= 0 && position < provinceList.size()) {
            provinceList.remove(position);
            mEditor.putString(Constant.PROVINCE, ListToJson.toJson(provinceList).toString());
            mEditor.commit();
            notifyDataSetChanged();
        }
    }

    @Override
    public void dragEnd() {
        // TODO: 16-3-26 拖动完成的回调
        int position = 0;
        for (int i = 0; i < provinceList.size(); i++) {
            if (selectItem.getId() == provinceList.get(i).getId()) {
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

        public void exchangeOtherAdapter(List<ProvinceItem> data, int position);

        public void setCurrentPosition();
    }

    private class ViewHolder {
        private View view;
        private TextView textView;
    }

}
