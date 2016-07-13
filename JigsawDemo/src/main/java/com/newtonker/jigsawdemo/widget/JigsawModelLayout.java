package com.newtonker.jigsawdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.event.OnFilterItemClickListener;
import com.newtonker.jigsawdemo.event.OnItemClickListener;
import com.newtonker.jigsawdemo.model.JigsawType;
import com.newtonker.jigsawdemo.model.TemplateEntity;
import com.newtonker.jigsawdemo.utils.TemplateUtils;
import com.newtonker.jigsawdemo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class JigsawModelLayout extends RelativeLayout {
    private static final float DRAG_SCALE = 1.05f;

    private Context context;

    // 记录当前的选中框
    private Rect curRect;
    // 当前选中的组件
    private JigsawModelView curModelView;
    // 当前选中的图片的id
    private int curSelection;
    // 记录当前的显示状态
    private boolean showSelected = false;

    // 弹窗
    private JigsawPopupWindow popupWindow;

    private List<String> imagePaths;
    private List<String> positionList;
    // 缓存TouchSlotView的状态
    private List<Bundle> bundleList;
    // 缓存TouchSlotView的坐标
    private List<Rect> boundaryList;
    private List<JigsawModelView> modelViewList;

    // 点击时候的X位置
    private int downX;
    // 点击时候的Y位置
    private int downY;
    // 点击时候对应整个界面的X位置
    private int windowX;
    // 点击时候对应整个界面的Y位置
    private int windowY;
    // 点击的时候，点击的点距离距离当前图片左边界和上边界的距离
    private int winViewX;
    private int winViewY;

    // 拖动标志位，如果正在移动，事件部分发给子类，很重要的标志位
    private boolean isMoving = false;

    private ImageView dragImageView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;


    ///////////////////////
    private TemplateEntity entity;
    private int modelAreaParentWidth;
    private int modelAreaParentHeight;

    public void setModelAreaParentWidthAndHeight(int modelAreaParentWidth, int modelAreaParentHeight) {
        this.modelAreaParentWidth = modelAreaParentWidth;
        this.modelAreaParentHeight = modelAreaParentHeight;
    }


    public JigsawModelLayout(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.touch_slot_layout, this);

        init(true);
    }

    // 这个构造方法在显示一组拼图列表时使用
    // 展示单个拼图模板，用其他三个
    // 当显示一组拼图列表时，如果初始化popupWindow，比较耗时，会出错
    public JigsawModelLayout(Context context, boolean showPopup) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.touch_slot_layout, this);

        init(showPopup);
    }

    public JigsawModelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.touch_slot_layout, this);

        init(true);
    }

    public JigsawModelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.touch_slot_layout, this);

        init(true);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        for (int i = bundleList.size() - 1; i >= 0; i--) {
            modelViewList.get(i).restoreInstanceState(bundleList.get(i));
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        if (null == bundleList) {
            bundleList = new ArrayList<>();
        } else {
            bundleList.clear();
        }

        for (JigsawModelView slotView : modelViewList) {
            bundleList.add(slotView.saveInstanceState());
        }

        return super.onSaveInstanceState();
    }

    private void init(Boolean showPopup) {
        if (showPopup) {
            // 初始化popupwindow
            popupWindow = new JigsawPopupWindow(context);
        }
        boundaryList = new ArrayList<>();
        modelViewList = new ArrayList<>();
        bundleList = new ArrayList<>();
    }

    /**
     * 隐藏弹窗
     */
    public void dismissPopup() {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 设置滤镜列表侦听
     *
     * @param onFilterItemClickListener
     */
    public void setOnPopupFilterItemClickListener(final OnFilterItemClickListener onFilterItemClickListener) {
        if (null == popupWindow) {
            return;
        }

        popupWindow.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GPUImageFilter filter) {
                // 设置当前选中的Filter的position
                curModelView.setCurFilterPosition(position);

                if (null != onFilterItemClickListener) {
                    onFilterItemClickListener.onItemClick(view, position, filter);
                }
            }
        });
    }

    /**
     * 设置图片替换按钮侦听
     *
     * @param onItemClickListener
     */
    public void setOnPopupSelectListener(final OnItemClickListener onItemClickListener) {
        if (null == popupWindow) {
            return;
        }

        popupWindow.setSelectListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(v, curSelection);
                }
            }
        });
    }

    /**
     * 设置图片列表
     *
     * @param paths
     */
    public void setImagePathList(List<String> paths) {
        if (null != paths) {
            imagePaths = paths;
        }
    }

    /**
     * 设置位置列表
     */
    public void setTemplateEntity(TemplateEntity entity) {
        this.entity = entity;
        setTemplateEntity();
    }

    public void setTemplateEntity() {
        if (null != entity) {
            positionList = str2Coor(entity.getPoints(), entity.getPolygons());
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            downX = (int) ev.getX();
            downY = (int) ev.getY();
            windowX = (int) ev.getRawX();
            windowY = (int) ev.getRawY();

            // 显示悬浮窗
            if (null == curRect || !curRect.contains(downX, downY) || (null != popupWindow && !popupWindow.isShowing())) {
                int position = pointToPosition(downX, downY);
                if (position >= 0) {
                    curSelection = position;
                    curRect = boundaryList.get(position);
                    curModelView = modelViewList.get(position);
                    curModelView.setIsDrawBoundary(true);
                }
            }
        }

        return isMoving;
    }

    /**
     * 当前移动点所对应的位置
     *
     * @param x
     * @param y
     * @return
     */
    private int pointToPosition(int x, int y) {
        int targetPosition = -1;

        for (int i = boundaryList.size() - 1; i >= 0; i--) {
            Rect rect = boundaryList.get(i);
            if (rect.contains(x, y)) {
                targetPosition = i;

                break;
            }
        }

        return targetPosition;
    }

    /**
     * 设置是否显示选中状态
     *
     * @param showSelected
     */
    public void setShowSelectedState(boolean showSelected) {
        this.showSelected = showSelected;

        if (!showSelected) {
            // 隐藏悬浮窗
            dismissPopup();

            for (JigsawModelView jigsawModelView : modelViewList) {
                jigsawModelView.setIsDrawBoundary(false);
                jigsawModelView.invalidate();
            }
        }
    }

    /**
     * 获取当前的选中状态
     *
     * @return
     */
    public boolean getShowSelectedState() {
        return showSelected;
    }

    /**
     * 替换选中的图片
     *
     * @param path
     */
    public void replaceSelectedBitmap(String path) {
        if (null != curModelView) {
            curModelView.resetOriginBitmap();
            curModelView.setCurFilterPosition(0);
            // 加载图片
            Glide.with(context).load(path).asBitmap().into(curModelView);
        }
    }

    /**
     * 渲染当前选中的图片
     *
     * @param bitmap
     */
    public void renderSelectedBitmap(Bitmap bitmap) {
        curModelView.setImageBitmap(bitmap);
    }

    /**
     * 获取当前选中组件中的图片
     *
     * @return
     */
    public Bitmap getSelectedBitmap() {
        return curModelView.getOriginBitmap();
    }


    /**
     * 重绘拼图界面
     */
    public void reDraw(int x, int y) {
        this.modelAreaParentWidth = x;
        this.modelAreaParentHeight = y;
        reDraw();
    }

    /**
     * 通过points和polygons两个参数去获取图片应该放置的位置
     *
     * @param pointsString   解析XML获取的points标签的字符串
     * @param polygonsString 解析XML获取的polygons标签的字符串
     * @return 同一个slot下其中一个模版所有图片的位置集合
     */
    private List<String> str2Coor(String pointsString, String polygonsString) {
        List<String> coordinateStringList = new ArrayList<>();
        // 0:0 0.5:0 1:0 0:1 0.5:1 1:1
        String[] pointsStr = pointsString.split(",");
        // 0,1,4,3     1,2,5,4
        String[] polygonsStr = polygonsString.split("/");

        Log.e("str2Coor", "start to turn");
        // 将环绕规则的字符串转成Int,作为判断坐标的角标
        for (String pStr : polygonsStr) {
            // "0" "1" "4" "3"
            String[] cStr = pStr.split(",");
            // 0 1 4 3
            int[] cInt = new int[cStr.length];
            for (int i = 0; i < cInt.length; i++) {
                cInt[i] = Integer.parseInt(cStr[i]);
            }
            for (int pInt : cInt) {
                // "0:0" "0.5:0" "0.5:1" "0:1"
                coordinateStringList.add(pointsStr[pInt]);
                Log.e("str2Coor", "coordinateStringList =" + coordinateStringList.size() + "value = " + pointsStr[pInt]);
            }
        }

        return coordinateStringList;
    }


    public void reDraw() {
        if (null == positionList || null == imagePaths || 0 == modelAreaParentWidth || 0 == modelAreaParentHeight) {
            return;
        }

        if (null == boundaryList) {
            boundaryList = new ArrayList<>();
        } else {
            boundaryList.clear();
        }

        if (null == modelViewList) {
            modelViewList = new ArrayList<>();
        } else {
            modelViewList.clear();
        }

        // remove all views before add
        this.removeAllViews();

        int left, top, right, bottom;
        for (int i = 0; i < imagePaths.size(); i++) {
            JigsawModelView singleTouchView = new JigsawModelView(context);

            left = (int) ((Float.parseFloat(positionList.get(i * 4).split(":")[0])) * modelAreaParentWidth);
            top = (int) ((Float.parseFloat(positionList.get(i * 4).split(":")[1])) * modelAreaParentHeight);
            right = (int) ((Float.parseFloat(positionList.get(i * 4 + 2).split(":")[0])) * modelAreaParentWidth);
            bottom = (int) ((Float.parseFloat(positionList.get(i * 4 + 2).split(":")[1])) * modelAreaParentHeight);

            Log.d("info", "num = " + i + "=========");

            Log.d("info", "left = " + positionList.get(i * 4).split(":")[0]);
            Log.d("info", "top = " + positionList.get(i * 4).split(":")[1]);
            Log.d("info", "right = " + positionList.get(i * 4 + 2).split(":")[0]);
            Log.d("info", "bottom = " + positionList.get(i * 4 + 2).split(":")[1]);

            int childWidth = Math.abs(right - left);
            int childHeight = Math.abs(bottom - top);

            LayoutParams layoutParams = new LayoutParams(childWidth, childHeight);
            layoutParams.leftMargin = left;
            layoutParams.topMargin = top;
            singleTouchView.setLayoutParams(layoutParams);
            // 设置侦听
            singleTouchView.setOnClickListener(itemOnClickListener);
            singleTouchView.setOnLongClickListener(itemOnLongClickListener);

            this.addView(singleTouchView);
            // 加载图片
            Log.d("path", "path i= " + i + "   " + imagePaths.get(i));
            Glide.with(context).load(imagePaths.get(i)).asBitmap().into(singleTouchView);

            // view 边界加入list中
            boundaryList.add(new Rect(left, top, 2 * right, 2 * bottom));
            modelViewList.add(singleTouchView);
        }
    }

    // 单个图片的单击后弹窗
    private OnClickListener itemOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != popupWindow) {
                // 显示弹窗
                popupWindow.showAsDropDown(curModelView);
                // 设置当前选中的position位置
                popupWindow.setSelectedFilterPosition(curModelView.getCurFilterPosition());
                // 设置当前的显示状态
                showSelected = true;
            }
        }
    };

    // 单个图片长按侦听
    private OnLongClickListener itemOnLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            // 获取影像
            Bitmap bm = curModelView.getDrawingCacheBitmap();
            // 初始化参数
            winViewX = downX - v.getLeft();
            winViewY = downY - v.getTop();

            // 开始拖拽
            startDrag(bm, windowX, windowY);
            // 隐藏当前图片
            ViewUtils.viewFadeOut(context, curModelView, 0);
            // 设置拖动标志位
            isMoving = true;

            return true;
        }
    };

    /**
     * 拖拽开始
     *
     * @param bm
     * @param x
     * @param y
     */
    private void startDrag(Bitmap bm, int x, int y) {
        stopDrag();
        // 获取windows界面
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        // 得到preview左上角相对于屏幕的坐标
        windowParams.x = x - winViewX;
        windowParams.y = y - winViewY;
        // 设置拖拽item的宽和高
        windowParams.width = (int) (DRAG_SCALE * bm.getWidth());
        windowParams.height = (int) (DRAG_SCALE * bm.getHeight());
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView, windowParams);

        dragImageView = imageView;
    }

    /**
     * 拖拽结束
     */
    private void stopDrag() {
        if (null != dragImageView) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != dragImageView) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) ev.getX();
                    downY = (int) ev.getY();
                    windowX = (int) ev.getRawX();
                    windowY = (int) ev.getRawY();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    onDrag((int) ev.getRawX(), (int) ev.getRawY());
                    return true;

                case MotionEvent.ACTION_UP:
                    stopDrag();
                    onDrop(x, y);
                    return true;

                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 设置拖拽view的位置
     *
     * @param rawX
     * @param rawY
     */
    private void onDrag(int rawX, int rawY) {
        if (null != dragImageView) {
            windowParams.alpha = 0.6f;
            windowParams.x = rawX - winViewX;
            windowParams.y = rawY - winViewY;

            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }

    /**
     * 松开手之后判断是否要交换
     *
     * @param x
     * @param y
     */
    private void onDrop(int x, int y) {
        // 判断是否需要交换
        int position = pointToPosition(x, y);

        if (position >= 0 && position != curSelection) {
            // 交换
            exchangeViews(position, curSelection);
        }

        // 显示之前隐藏的view
        ViewUtils.viewFadeIn(context, curModelView, 0);
        // 设置拖动标志位
        isMoving = false;


    }

    /**
     * 交换两个view的图片
     *
     * @param i
     * @param j
     */
    private void exchangeViews(int i, int j) {
        // 交换两个位置的图片
        Collections.swap(imagePaths, i, j);

        JigsawModelView jigsawModelView;
        // 设置position i
        jigsawModelView = modelViewList.get(i);
        jigsawModelView.resetOriginBitmap();
        jigsawModelView.setCurFilterPosition(0);
        Glide.with(context).load(imagePaths.get(i)).asBitmap().into(jigsawModelView);

        // 设置position j
        jigsawModelView = modelViewList.get(j);
        jigsawModelView.resetOriginBitmap();
        jigsawModelView.setCurFilterPosition(0);
        Glide.with(context).load(imagePaths.get(j)).asBitmap().into(jigsawModelView);
    }

}