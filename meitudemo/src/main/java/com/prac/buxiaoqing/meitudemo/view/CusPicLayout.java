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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
    private HashMap<Integer, CusNumLayout> cusNumLayouts = new HashMap<>();

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
        width = 660;
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
        changeView();

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

    private boolean isNewLine = false;

    /**
     * 计算释放的位置
     *
     * @param x
     * @param y
     * @return
     */
    private int[] calDropPos(int x, int y) {

        isNewLine = false;

        int[] pos = new int[]{0, 0};

        PicEntity dragEntity = getDragEntity();
        int oldPosY = dragEntity.getPosY();

        log(" move:  onDrop x = " + x + "   y = " + y);
        int x_in_view = x - getLeft();
        int y_in_view = y - getTop();

        float newPosx = x_in_view / (width / MAX_NUM_IN_LINE * 1f);//  0 -- 2
        //float newPosy = y_in_view / (lineHeight * 1f);  // 0 --  curLines
        float newPosy = y_in_view;
        log("newPosY = " + newPosy);
        int height = 0;
        Iterator<Map.Entry<Integer, CusNumLayout>> iterator = cusNumLayouts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CusNumLayout> next = iterator.next();
            //////是不是按顺序来的呢   答案是肯定的
            int lastHeight = height;
            height += next.getValue().getLineHeight();

            if (y_in_view < height && y_in_view > lastHeight) {
                newPosy = (y_in_view - lastHeight) / (1f * next.getValue().getLineHeight()) + next.getKey();
                if (newPosy < 0)
                    newPosy = 0;

                log("newPosY = " + newPosy + "  y_in_view= " + y_in_view + "  height = " + height + "   lastHeight = " + lastHeight + "  next.getKey() = " + next.getKey());
                //TODO
                break;
            } else if (y_in_view > height) {
                newPosy = (y_in_view - height) / (1f * next.getValue().getLineHeight()) + next.getKey() + 1;
                log("newPosY = " + newPosy + "  y_in_view= " + y_in_view + "  height = " + height + "  next.getKey() = " + next.getKey());
                break;
            }
        }
        int indexX = (int) (newPosx * 10) % 10;
        /////////
        if (indexX > 3 && indexX < 7)
            newPosx = (int) newPosx;
        if (indexX > 7)
            newPosx = (int) newPosx + 1;
        if (indexX < 3 && newPosx >= 1)
            newPosx = (int) newPosx - 1;

        newPosx = (int) newPosx;

        if (newPosx >= 2)
            newPosx = 2f;

        newPosy = (int) newPosy;
        //最后一行如果只有一个可不能再起一行   其他行都可以新起一行
        if (newPosy >= curLines && oldPosY < curLines - 1) {
            isNewLine = true;
            newPosy = curLines;
        } else if (newPosy >= curLines && oldPosY == curLines - 1) {
            if (cusNumLayouts.get(curLines - 1).getCurNum() == 1) {
                isNewLine = false;
                newPosy = oldPosY;
            } else {
                isNewLine = true;
                newPosy = curLines;
            }
        } else if ((int) newPosy > oldPosY && cusNumLayouts.get(oldPosY).getCurNum() == 1) {
            //一行只有一个,还给拖拽走了
            newPosy -= 1;
        }

        //Y 坐标的处理
        if (isNewLine) {
            pos[1] = curLines;
        } else {
            //  取小数部分的第一位
            //  0--3  是在偏下的一行
            //  4--7  挤出新的一行
            //  8--9  是在偏下的一行
            int indexY = (int) (newPosy * 10) % 10;
            if (indexY >= 3 && indexY <= 7) {
                isNewLine = true;
                newPosy = (int) newPosy + 1;
            } else if (indexY < 10 && indexY >= 8) {
                newPosy = (int) newPosy + 1;
            }
            pos[1] = (int) newPosy;
        }

        //X坐标处理
        if (isNewLine) {
            pos[0] = 0;
        } else {
            int curLineNum = cusNumLayouts.get((int) newPosy).getCurNum();//1    2     3
            if (newPosx < curLineNum) {
                pos[0] = (int) newPosx;
            } else {
                int newIndex = curLineNum;//后入式
                if (oldPosY == (int) newPosy) {
                    //同一行
                    isNewLine = false;
                    pos[0] = newIndex - 1;
                } else {
                    if (newIndex >= MAX_NUM_IN_LINE) {
                        pos[0] = 0;//满了的话   要进入下一行了,下一行的都要往后移动了,满了,再进入下一行   ,这个还没吧
                        newPosy = (int) newPosy + 1;
                        if (newPosy > curLines) {
                            newPosy = curLines;
                        }
                        pos[1] = (int) newPosy;
                    } else {
                        pos[0] = (int) newIndex;
                    }
                }
            }
        }


        return pos;
    }


    /**
     * ACTION_UP ,确定要改变的位置
     *
     * @param x
     * @param y
     */
    private void onDrop(int x, int y) {

        int oldPosX = dragEntity.getPosX();
        int oldPoxY = dragEntity.getPosY();

        int dragPos = getDraDataPos();

        int[] ints = calDropPos(x, y);

        PicEntity drag = picEntities.get(dragPos);
        drag.setPosX(ints[0]);
        drag.setPosY(ints[1]);

        for (int i = 0; i < curPics; i++) {
            PicEntity picEntity = picEntities.get(i);
            log("before move:   i= " + i + "  posX = " + picEntity.getPosX() + "  posY = " + picEntity.getPosY());
        }

        log("before move:   curLines= " + curLines);
        if (oldPoxY == drag.getPosY())
            isNewLine = false;


        log("move:  isNewLine= " + isNewLine + "  oldPoxY= " + oldPoxY + "  oldPosX= " + oldPosX + "   newY = "
                + drag.getPosY() + "   newX = " + drag.getPosX());


        changeDataPos(isNewLine, dragPos, oldPoxY, oldPosX, ints[1], ints[0]);

        for (int i = 0; i < curPics; i++) {
            PicEntity picEntity = picEntities.get(i);
            log("after move:   i= " + i + "  posX = " + picEntity.getPosX() + "  posY = " + picEntity.getPosY());
        }

        changeView();
    }

    /**
     * 这个方法是修改移动之后,除了移动的图片之外的数据的改动
     * 其实一次应该也就够了，插入到某一个位置（如果是新的行，那么后面的行的行数都要加1；
     * 如果是插入到旧行，插入行的列数做相应改变）
     * <p/>
     * <p/>
     * 有两种情况    前到后     后到前
     *
     * @param isNewLine 是否是独占一行
     * @param newY      移动图片的新Y坐标
     * @param newX      移动图片的新X坐标
     *                  move:  = true  oldPoxY= 0  oldPosX= 2   newY = 3   newX = 0
     */
    private void changeDataPos(boolean isNewLine, int dragPos, int oldY, int oldX, int newY, int newX) {

        for (int i = 0; i < curPics; i++) {
            //跳过拖拽的那个
            if (dragPos == i) {
                continue;
            }

            PicEntity picEntity = picEntities.get(i);

            if (newY == oldY) {
                if (picEntity.getPosY() == newY && picEntity.getPosX() == newX)
                    picEntity.setPosX(oldX);
            } else if (newY < oldY) {
                if (isNewLine) {
                    if (cusNumLayouts.get(oldY).getCurNum() == 1) {
                        if (picEntity.getPosY() > newY && picEntity.getPosY() < oldY)
                            picEntity.setPosY(picEntity.getPosY() + 1);
                    } else {
                        if (picEntity.getPosY() > newY)
                            picEntity.setPosY(picEntity.getPosY() + 1);

                        if (picEntity.getPosY() == oldY && picEntity.getPosX() > oldX)
                            picEntity.setPosX(picEntity.getPosX() - 1);
                    }
                } else {
                    if (cusNumLayouts.get(oldY).getCurNum() == 1) {
                        if (picEntity.getPosY() > oldY)
                            picEntity.setPosY(picEntity.getPosY() - 1);
                    } else {
                        if (picEntity.getPosY() == oldY && picEntity.getPosX() > oldX)
                            picEntity.setPosX(picEntity.getPosX() - 1);
                    }

                    int newLineNum = cusNumLayouts.get(newY).getCurNum();
                    if (newLineNum == MAX_NUM_IN_LINE) {
                        if (picEntity.getPosY() == newY && picEntity.getPosX() >= newX && picEntity.getPosX() < 2)
                            picEntity.setPosX(picEntity.getPosX() + 1);
                        else if (picEntity.getPosY() == newY && picEntity.getPosX() == 2) {  //满了  顺势后移
                            picEntity.setPosX(0);
                            picEntity.setPosY(picEntity.getPosY() + 1);
                            flowMove(i, picEntity.getPosY());
                        }
                    } else {
                        if (picEntity.getPosY() == newY && picEntity.getPosX() >= newX)
                            picEntity.setPosX(picEntity.getPosX() + 1);
                    }
                }
            } else {
                if (isNewLine) {
                    if (cusNumLayouts.get(oldY).getCurNum() == 1) {
                        if (picEntity.getPosY() > oldY && picEntity.getPosY() <= newY)
                            picEntity.setPosY(picEntity.getPosY() - 1);
                    } else {
                        if (picEntity.getPosY() >= newY)
                            picEntity.setPosY(picEntity.getPosY() + 1);

                        if (picEntity.getPosY() == oldY && picEntity.getPosX() > oldX)
                            picEntity.setPosX(picEntity.getPosX() - 1);
                    }
                } else {
                    if (cusNumLayouts.get(oldY).getCurNum() == 1) {
                        if (picEntity.getPosY() > oldY)
                            picEntity.setPosY(picEntity.getPosY() - 1);
                    } else {
                        if (picEntity.getPosY() == oldY && picEntity.getPosX() > oldX)
                            picEntity.setPosX(picEntity.getPosX() - 1);
                    }

                    int newLineNum = cusNumLayouts.get(newY).getCurNum();
                    if (newLineNum == MAX_NUM_IN_LINE) {
                        if (picEntity.getPosY() == newY && picEntity.getPosX() >= newX && picEntity.getPosX() < 2)
                            picEntity.setPosX(picEntity.getPosX() + 1);

                        //满了  顺势后移
                        if (picEntity.getPosY() == newY && picEntity.getPosX() == 2) {
                            picEntity.setPosX(0);
                            picEntity.setPosY(picEntity.getPosY() + 1);
                            flowMove(i, picEntity.getPosY());
                        }
                    } else {
                        if (picEntity.getPosY() == newY && picEntity.getPosX() >= newX)
                            picEntity.setPosX(picEntity.getPosX() + 1);
                    }
                }
            }
        }
    }

    /**
     * 前一行满3了,向后平移
     *
     * @param x
     * @param y
     */
    private void flowMove(int k, int y) {
        for (int i = 0; i < curPics; i++) {
            PicEntity picEntity = picEntities.get(i);
            if (i == k)
                continue;
            if (picEntity.getPosY() == y) {
                if (picEntity.getPosX() + 1 <= MAX_NUM_IN_LINE - 1)
                    picEntity.setPosX(picEntity.getPosX() + 1);
                else {
                    //溢出了
                    picEntity.setPosX(0);
                    picEntity.setPosY(picEntity.getPosY() + 1);
                    flowMove(i, picEntity.getPosY());
                }
            }
        }
    }

    /**
     * 最好是用getDraDataPos(),改变位置之后,dragEntity.getPosX(), dragEntity.getPosY()坐标的可能会有两个
     *
     * @return
     */
    private PicEntity getDragEntity() {
        PicEntity drag = null;
        if (dragEntity != null)
            for (PicEntity picEntity : picEntities) {
                if (picEntity.getPosY() == dragEntity.getPosY() && picEntity.getPosX() == dragEntity.getPosX())
                    drag = picEntity;
            }
        return drag;
    }

    private int getDraDataPos() {
        int dragPos = 0;
        for (int i = 0; i < curPics; i++) {
            PicEntity picEntity = picEntities.get(i);
            if (picEntity.getPosY() == dragEntity.getPosY() && picEntity.getPosX() == dragEntity.getPosX())
                dragPos = i;
        }
        return dragPos;
    }


    private int moveX, moveY;

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
        int[] ints = calDropPos((int) rawX, (int) rawY);

        if (moveX != ints[0])
            moveX = ints[0];

        if (moveY != ints[1])
            moveY = ints[1];

        //坐标怎么弄 TODO
        move(moveX, moveY);

        log("new pos  x = " + ints[0] + "  y = " + ints[1] + "  isNewLine = " + isNewLine + "    origin  x = " + rawX + "   y = " + rawY);
    }

    public void move(int x, int y) {
        if (isNewLine) {
            if (y - 1 >= 0) {
                View childUp = parentLinearLayout.getChildAt(y - 1);
                if (childUp != null) {
                    childUp.clearAnimation();
                    childUp.startAnimation(moveUp());
                }
            }

            if (y + 1 < curLines) {
                View childBelow = parentLinearLayout.getChildAt(y + 1);
                if (childBelow != null) {
                    childBelow.clearAnimation();
                    childBelow.startAnimation(moveDown());
                }
            }
        } else {
            if (y >= curLines)
                return;

            if (x >= cusNumLayouts.get(y).getCurNum())
                return;

            cusNumLayouts.get(y).avoid(x);
        }
    }

    private TranslateAnimation moveUp() {
        TranslateAnimation moveUp = new TranslateAnimation(0, 0, 0, -100);
        moveUp.setDuration(200);
        moveUp.setFillAfter(false);
        moveUp.setRepeatMode(Animation.REVERSE);
        moveUp.setRepeatCount(1);
        moveUp.start();
        return moveUp;
    }

    private TranslateAnimation moveDown() {
        TranslateAnimation moveDown = new TranslateAnimation(0, 0, 0, 100);
        moveDown.setDuration(200);
        moveDown.setFillAfter(false);
        moveDown.setRepeatMode(Animation.REVERSE);
        moveDown.setRepeatCount(1);
        moveDown.start();
        return moveDown;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent");
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

                    win_view_x = (int) ev.getRawX() - picEntity.getPosX() * width / cusNumLayouts.get(picEntity.getPosY()).getCurNum();//点击在VIEW上的相对位置
                    win_view_y = (int) ev.getRawY() - picEntity.getPosY() * lineHeight;//点击在VIEW上的相对位置   有问题

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
        cusNumLayouts.clear();
        parentLinearLayout.removeAllViews();
    }

    /**
     * 布局变了之后,重新加载数据
     */
    private void reLoadLineDatas() {
        clearLinesDatas();
        for (int i = 0; i < curPics; i++) {
            PicEntity picEntity = picEntities.get(i);
            int key = picEntity.getPosY();
            if (cusNumLayouts.get(key) == null) {
                CusNumLayout cl = new CusNumLayout(context);
                cl.setLayoutParams(lineParams);
                cl.setParentSize(width);
                cl.getAddDatas().put(picEntity.getPosX(), picEntity);
                cusNumLayouts.put(key, cl);
                log(" reLoadLineDatas i = " + i + "  行 = " + key + "  列 = " + picEntity.getPosX());
            } else {
                cusNumLayouts.get(key).getAddDatas().put(picEntity.getPosX(), picEntity);
                log(" reLoadLineDatas i = " + i + "  行 = " + key + "  列 = " + picEntity.getPosX());
            }
        }

        for (int i = 0; i < cusNumLayouts.size(); i++) {
            log(" check data 行= " + i + "  列 = " + cusNumLayouts.get(i).getAddDatas().size() + "个");
        }
    }

    public void changeView() {
        reLoadLineDatas();
        curLines = cusNumLayouts.size();
        for (int i = 0; i < curLines; i++) {
            CusNumLayout cusNumLayout = cusNumLayouts.get(i);
            cusNumLayout.setDatas(i);
            cusNumLayout.buildView();
            parentLinearLayout.addView(cusNumLayouts.get(i));
            log("刷新行  =" + i);
            log("该行有  =" + cusNumLayout.getCurNum() + " 张");
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
