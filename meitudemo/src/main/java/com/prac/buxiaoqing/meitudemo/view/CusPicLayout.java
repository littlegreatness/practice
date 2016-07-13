package com.prac.buxiaoqing.meitudemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.prac.buxiaoqing.meitudemo.model.PicEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author：buxiaoqing on 16/7/11 14:05
 * Just do IT(没有梦想,何必远方)
 */
public class CusPicLayout extends ScrollView {

    private static final String TAG = CusPicLayout.class.getSimpleName();
    private static final int MAX_NUM_IN_LINE = 3;//一行最多3张

    private Context context;
    private LinearLayout parentLinearLayout;//ScrollView下面的LinearLayout
    private int width, height;
    private int curPics; //总共有几张图 == picEntities.size();
    private ArrayList<PicEntity> picEntities;
    private int curLines;//总共分了几行   == cusNumLayouts.size();
    private int lineHeight;
    private ArrayList<CusNumLayout> cusNumLayouts = new ArrayList<>();

    private LinearLayout.LayoutParams lineParams;

    ///////////////////////////////////////////
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;
    private PicEntity dragEntity;
    private ImageView dragView;
    private double dragScale = 1.2d;

    /**
     * 点击时对应整个界面的X/Y位置
     **/
    public int windowX;
    public int windowY;
    /**
     * 触摸点在View上的X/Y位置
     **/
    private int win_view_x;
    private int win_view_y;

    public CusPicLayout(Context context) {
        this(context, null);
    }

    public CusPicLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusPicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(parentParams);

        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
        width = 650;
    }

    public List<PicEntity> getPicEntities() {
        return picEntities;
    }

    public void setPicEntities(ArrayList<PicEntity> picEntities) {
        this.picEntities = picEntities;
        setCurPics(picEntities.size());
    }

    public int getCurLines() {
        return curLines;
    }

    public void setCurLines(int curLines) {
        this.curLines = curLines;
    }

    public int getCurPics() {
        return curPics;
    }

    public void setCurPics(int curPics) {
        this.curPics = curPics;
        if (curPics % MAX_NUM_IN_LINE == 0) {
            curLines = curPics / MAX_NUM_IN_LINE;
        } else {
            curLines = curPics / MAX_NUM_IN_LINE + 1;
        }
        //TODO  先做成方形的,并且高度都一致,后面再改这些吧
        log("curLines = " + curLines + "   height=" + height);
    }

    private void setParentParams() {
        height = curLines * width / MAX_NUM_IN_LINE;
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(width, height);
        parentLinearLayout = new LinearLayout(context);
        parentLinearLayout.setLayoutParams(parentParams);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(parentLinearLayout);
    }

    public void initView() {
        setParentParams();
        lineHeight = width / MAX_NUM_IN_LINE;
        lineParams = new LinearLayout.LayoutParams(width, lineHeight);
        for (int i = 0; i < curLines; i++) {
            CusNumLayout cusNumLayout = new CusNumLayout(context);
            cusNumLayout.setLayoutParams(lineParams);
            cusNumLayout.setParentSize(width, width / MAX_NUM_IN_LINE);
            ArrayList<PicEntity> entities = new ArrayList<>();
            if (i == curLines - 1)
                for (int j = i * MAX_NUM_IN_LINE; j < curPics; j++) {
                    entities.add(picEntities.get(j));
                }
            else
                for (int j = i * MAX_NUM_IN_LINE; j <= (i + 1) * MAX_NUM_IN_LINE - 1; j++) {
                    entities.add(picEntities.get(j));
                }
            cusNumLayout.setDatas(entities, i);
            cusNumLayout.setLineHeight(lineHeight);
            cusNumLayout.buildView();
            parentLinearLayout.addView(cusNumLayout);
            cusNumLayouts.add(i, cusNumLayout);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        log("onTouchEvent");
        if (dragView != null) {
            log("dragView != null");
            windowX = (int) ev.getRawX();
            windowY = (int) ev.getRawY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    log("ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    log("ACTION_MOVE");
                    onDrag(windowX, windowY);
                    onMove(windowX, windowY);
                    break;
                case MotionEvent.ACTION_UP:
                    log("ACTION_UP");
                    stopDrag();
                    onDrop(windowX, windowY);
                    requestDisallowInterceptTouchEvent(false);
                    break;
                default:
                    break;
            }
        } else
            log("dragView 空了");
        return super.onTouchEvent(ev);
    }

    /**
     * ACTION_MOVE 其它位置的图片要稍微让步以下的动画
     *
     * @param windowX
     * @param windowY
     */
    private void onMove(int windowX, int windowY) {


    }

    /**
     * ACTION_UP ,确定要改变的位置
     *
     * @param x
     * @param y
     */
    private void onDrop(int x, int y) {
        log("onDrop x = " + x + "   y = " + y);
        int x_in_view = x - getLeft();
        int y_in_view = y - getTop();

        int newPosx = x_in_view / (width / MAX_NUM_IN_LINE);
        int newPposy = y_in_view / lineHeight;

        log("onDrop newPosx = " + newPosx + "   newPposy = " + newPposy);

        PicEntity dragEntity = getDragEntity();
        cusNumLayouts.get(dragEntity.getPosY()).getDatas().remove(dragEntity.getPosX());

        if (newPposy >= curLines) {
            //新起一行
            CusNumLayout cusNumLayout = new CusNumLayout(context);
            cusNumLayout.setLayoutParams(lineParams);
            cusNumLayout.setParentSize(width, width / MAX_NUM_IN_LINE);
            dragEntity.setPosX(0);
            dragEntity.setPosY(curLines);
            ArrayList<PicEntity> entities = new ArrayList<>();
            entities.add(dragEntity);
            cusNumLayout.setDatas(entities, curLines);
            cusNumLayout.buildView();
            parentLinearLayout.addView(cusNumLayout);
            cusNumLayouts.add(curLines, cusNumLayout);
        } else {
            CusNumLayout cusNumLayout = cusNumLayouts.get(newPposy);
            int curLineNum = cusNumLayout.getDatas().size();
            dragEntity.setPosY(newPposy);
            if (curLineNum == 1) {
                if (curLineNum <= newPosx) {
                    //插到第二个位置
                    dragEntity.setPosX(1);
                    cusNumLayout.getDatas().add(1, dragEntity);
                } else {
                    //插到第一个位置   后面的位置要+1
                    dragEntity.setPosX(0);
                    cusNumLayout.getDatas().get(0).setPosX(1);
                    cusNumLayout.getDatas().add(0, dragEntity);
                }
            } else if (curLineNum == 2) {
                if (curLineNum <= newPosx) {
                    //插到第三个位置
                    dragEntity.setPosX(2);
                    cusNumLayout.getDatas().add(2, dragEntity);
                } else {
                    if (newPosx == 0) {
                        //插到第一个位置   后面的位置要+1
                        dragEntity.setPosX(0);
                        cusNumLayout.getDatas().get(0).setPosX(1);
                        cusNumLayout.getDatas().get(1).setPosX(2);
                        cusNumLayout.getDatas().add(0, dragEntity);
                    } else {
                        //插到第二个位置  后面的位置要+1
                        dragEntity.setPosX(1);
                        cusNumLayout.getDatas().get(1).setPosX(2);
                        cusNumLayout.getDatas().add(1, dragEntity);
                    }
                }
            } else if (curLineNum == 3) {
                //就不处理了
            }
        }
        //changeView();
    }

    private PicEntity getDragEntity() {
        if (dragEntity != null)
            return cusNumLayouts.get(dragEntity.getPosY()).getDatas().get(dragEntity.getPosX());
        else
            return null;
    }

    /**
     * 选择的图片会跟着触摸点走
     *
     * @param rawX
     * @param rawY
     */
    private void onDrag(float rawX, float rawY) {
        if (dragView != null) {
            windowParams.alpha = 1.f;
            windowParams.x = (int) rawX - win_view_x;
            windowParams.y = (int) rawY - win_view_y;
            log("onDrag x = " + rawX + "   y = " + rawY);
            windowManager.updateViewLayout(dragView, windowParams);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent");
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = 0; i < picEntities.size(); i++) {
                if (picEntities.get(i).isSelected()) {
                    //获取长按的位置   为什么不是在ACTION_DOWN获取到的呢???
                    log("onLongclick move  i= " + i + "  posX = " + picEntities.get(i).getPosX() + "  posY = " + picEntities.get(i).getPosY());
                    //根据坐标获取指定位置图片
                    PicEntity picEntity = picEntities.get(i);

                    if (dragEntity == null)
                        dragEntity = new PicEntity(picEntity.getResId());
                    dragEntity.setPosX(picEntity.getPosX());
                    dragEntity.setPosY(picEntity.getPosY());

                    ImageView imageView = picEntity.getImageView();

                    win_view_x = x - imageView.getLeft();//点击在VIEW上的相对位置
                    win_view_y = y - imageView.getTop();//点击在VIEW上的相对位置

                    log("win_view_x = " + win_view_x);
                    log("win_view_y = " + win_view_y);

                    Bitmap drawingCache = creatCacheImg(picEntity.getResId());
                    startDrag(drawingCache, (int) ev.getRawX(), (int) ev.getRawY());
                    return true;
                }
            }
        }


        return super.onInterceptTouchEvent(ev);
    }

    private void clearSelected() {
        for (int i = 0; i < picEntities.size(); i++) {
            if (picEntities.get(i).isSelected()) {
                picEntities.get(i).setSelected(false);
            }
        }
    }

    private Bitmap creatCacheImg(String path) {
        Bitmap BitmapOrg = BitmapFactory.decodeFile(path);
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float sample = height / (width * 1f);

        int newWidth, newHeight;
        //长  大于   宽    长等于lineHeight,宽 缩放
        if (sample > 1) {
            newHeight = lineHeight;
            newWidth = (int) (lineHeight / sample);
        } else {
            newWidth = lineHeight;
            newHeight = (int) (lineHeight * sample);
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;

    }

    private void stopDrag() {
        if (dragView != null) {
            windowManager.removeView(dragView);
            dragView = null;
        }
        clearSelected();
    }

    /**
     * 开始拖拽
     *
     * @param dragBitmap
     * @param x
     * @param y
     */
    public void startDrag(Bitmap dragBitmap, int x, int y) {

        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowParams.x = x - win_view_x;
        windowParams.y = y - win_view_y;

        windowParams.width = (int) (dragScale * dragBitmap.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
        windowParams.height = (int) (dragScale * dragBitmap.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
        this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        this.windowParams.format = PixelFormat.TRANSLUCENT;
        this.windowParams.windowAnimations = 0;
        ImageView iv = new ImageView(context);
        iv.setImageBitmap(dragBitmap);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);// "window"
        windowManager.addView(iv, windowParams);
        dragView = iv;
        if (dragEntity != null)
            dragEntity.setImageView(dragView);
    }


    /**
     * 清理每行的数据
     */
    private void clearLinesDatas() {
        for (int i = 0; i < cusNumLayouts.size(); i++) {
            cusNumLayouts.clear();
        }
        this.removeAllViews();
        parentLinearLayout.removeAllViews();
    }

    /**
     * 布局变了之后,重新加载数据
     */
    private void reLoadLineDatas() {
        clearLinesDatas();
        HashMap<Integer, CusNumLayout> hashMap = new HashMap<>();
        for (int i = 0; i < curPics; i++) {
            PicEntity picEntity = picEntities.get(i);
            int key = picEntity.getPosY();
            if (hashMap.get(key) == null) {
                CusNumLayout cl = new CusNumLayout(context);
                cl.setLayoutParams(lineParams);
                cl.setParentSize(width, width / MAX_NUM_IN_LINE);
                cl.getDatas().add(picEntity.getPosX(), picEntity);
                hashMap.put(key, cl);
            } else {
                hashMap.get(key).getDatas().add(picEntity.getPosX(), picEntity);
            }
        }

        Iterator<Map.Entry<Integer, CusNumLayout>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CusNumLayout> next = iterator.next();
            cusNumLayouts.add(next.getKey(), next.getValue());
        }
    }

    public void changeView() {
        //reLoadLineDatas();
        curLines = cusNumLayouts.size();
        //setParentParams();
        parentLinearLayout.removeAllViews();
        for (int i = 0; i < curLines; i++) {
            CusNumLayout cusNumLayout = cusNumLayouts.get(i);
            cusNumLayout.setDatas(cusNumLayout.getDatas(), i);
            cusNumLayout.setLineHeight(lineHeight);
            cusNumLayout.buildView();
            parentLinearLayout.addView(cusNumLayouts.get(i));
            log("刷新行  =" + i);
            log("该行有  =" + cusNumLayout.getDatas().size() + " 张");
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
