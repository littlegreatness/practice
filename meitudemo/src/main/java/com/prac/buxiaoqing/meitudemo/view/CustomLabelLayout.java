package com.prac.buxiaoqing.meitudemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.prac.buxiaoqing.meitudemo.util.AndroidSysUtil;
import java.util.ArrayList;

/**
 * 自定义可扩展布局
 */
public class CustomLabelLayout extends ScrollView {
    //嗷嗷多的参数，如果调细节距离什么的，调这里
    public static final int BODY_SIZE_BY_SP = 15;
    public int BODY_COLOR = Color.BLACK;
    public static final int PADDING_BY_DIP = 0;
    public static final int BODY_HORIZONTAL_PADDING_BY_DIP = 8;
    public static final int SINGLE_LINE_HEIGHT_BY_DIP = 44; //实际内容区高度为 减去 2 x SINGLE_LINE_HEIGHT_PADDING_VERTICAL_BY_DIP
    public static final int SINGLE_LINE_HEIGHT_PADDING_VERTICAL_BY_DIP = 5;
    //******新加的一些参数*******
    //最小一行里的个数，不够的话，默认用空白的补
    public static final int MIN_CONTENT_COUNT = 3;
    public static final int MAX_CONTENT_COUNT = 5;
    public static final int AUTO_FILL_WEIGHT = 2;
    //预留0%的冗余空间
    public static final float SPACE_RETAINED_PERCENT = 0.0F;
    //如果剩余空间大于30%，继续追加标签
    public static final float SPACE_LEFT_ENOUGH_APPENDING_PERCENT = 0.3F;

    //控件宽度，用于计算补充空白的宽度
    private int widgetLength = 0;
    private int widgetHeight = 0;
    //点9主题图，非内容区的宽度总和，dp
    private int extWidthDp = 0;
    //加号控件的宽度
    private int addFlagWidgetWidth = 0;
    private Runnable runAfterOnlayout;
    private Runnable runAfterOnlayoutForPreSelection;
    public static final int MAX_SELECT_COUNT_DEFAULT = 10;
    private int maxSelectCount = MAX_SELECT_COUNT_DEFAULT;
    private Paint paint;

    private boolean isAllowLongClick = true;
    private boolean isFocusable = true;
    //到达最大限制之后是否允许滚动
    private boolean isAllowScroll = false;
    private int gravity = -1;
    //最大显示多少行，默认情况下，超出的做滚动处理
    private int maxLines = 0;
    //同时和maxLines起作用，两者达到1个即停止构建
    private int maxHeight = 0;
    //让控件自己计算应该有多高
    public static final int HEIGHT_MATCH_PARENT = -1;
    //******新加的一些参数*******

    //权重自动调平衡，不开的话只按字数来走(关了后果自负)
    public static final boolean SWITCHER_AUTO_BALANCE = true;
    //最大最小之间的允许的最大倍数差
    public static final float MAX_RATIO_BETWEEN_LARGEST_AND_SMALLEST = 1.5f;

    private Context context;
    private LinearLayout scrollChildLayout;

    private OnClickListener listener;
    private OuterBodyClickListener outerBodyListener;
    private OnDataFillingListener onDataFillingListener;

    private ArrayList<BodyEntity> list = new ArrayList<BodyEntity>();
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams windowParams = null;

    private Vibrator mVibrator;

    public CustomLabelLayout(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context, attrs, defStyle);
    }

    public CustomLabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs, 0);
    }

    public CustomLabelLayout(Context context) {
        super(context);
        this.context = context;
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        scrollChildLayout = new LinearLayout(context);
        scrollChildLayout.setOrientation(LinearLayout.VERTICAL);

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        scrollChildLayout.setLayoutParams(params);

        int paddingPx = AndroidSysUtil.PixelUtil.dp2px(context, PADDING_BY_DIP);
        setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx);

        addView(scrollChildLayout);
        setVerticalScrollBarEnabled(false);
    }


    /**
     * 监听body点击事件，内部点击逻辑会正常执行，可设置点击/非点击为相同资源ID来实现屏蔽选择状态切换的功能。
     * 传null，则取消外部监听
     * 和添加按钮的监听区分开
     *
     * @param outerBodyListener
     */
    public void setOuterBodyClickListener(OuterBodyClickListener outerBodyListener) {
        this.outerBodyListener = outerBodyListener;
    }

    /**
     * 设置数据到达最大行数的监听器
     *
     * @param onDataFillingListener
     */
    public void setOnDataFillingListener(OnDataFillingListener onDataFillingListener) {
        this.onDataFillingListener = onDataFillingListener;
    }

    public void removeOnDataFillingListener() {
        this.onDataFillingListener = null;
    }

    public void setAddFlagOnClickListener(OnClickListener lis) {
        listener = lis;
    }

    /**
     * 设置是否允许点击，不允许的话，内部点击事件也不执行了
     *
     * @param enableClick
     */
    public void setAllowLongClick(boolean enableClick) {
        if (isAllowLongClick == enableClick) {
            return;
        }
        isAllowLongClick = enableClick;
        //这里需要重新构建控件了
        rebuildAll();
    }

    /**
     * 到达最大限制之后是否允许滚动，如果能滚动，就会自动拦截触摸事件
     *
     * @param isAllowScroll
     */
    public void setIsAllowScroll(boolean isAllowScroll) {
        this.isAllowScroll = isAllowScroll;
    }

    public void setTouchEnable(boolean touchEnable) {
        super.setFocusable(touchEnable);
        isFocusable = touchEnable;
        setFocusableInternal(this);
    }

    private void setFocusableInternal(View parent) {
        parent.setFocusable(isFocusable);
        if (parent instanceof ViewGroup) {
            int childCount = ((ViewGroup) parent).getChildCount();
            if (childCount == 0) {
                return;
            }
            for (int index = 0; index < ((ViewGroup) parent).getChildCount(); index++) {
                View child = ((ViewGroup) parent).getChildAt(index);
                setFocusableInternal(child);
            }
        } else {
            return;
        }
    }

    private void rebuildAll() {
        if (widgetLength > 0) {
            //这里需要重新构建控件了
            ArrayList<String> bodyList = bodyArrToStringArr();
            addAllBody(bodyList.toArray(new String[0]));
            //去掉不活动的body实体
            destroyBodies();
        }
    }

    /**
     * 设置特定Gravity
     */
    public void setChildGravity(int gravity) {
        if (this.gravity == gravity) {
            return;
        }
        this.gravity = gravity;
        rebuildAll();
    }

    /**
     * 设置最大行数
     *
     * @param maxLines
     */
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    /**
     * 设置最大高度
     *
     * @param maxHeight 如果设置为{@link #HEIGHT_MATCH_PARENT}，控件高度自动变更为MATCH_PARENT
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        if (maxHeight == HEIGHT_MATCH_PARENT) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            setLayoutParams(params);
        }
    }

    /**
     * 指定控件宽度，为了不等View的OnLayout
     *
     * @param length
     */
    public void setWidgetLength(int length) {
        widgetLength = length;
    }

    public void destroy() {
        destroyBodies();
        list.clear();
        runAfterOnlayout = null;
    }

    private void destroyBodies() {
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if (!entity.isAlive) {
                list.remove(index);
                index--;
            }
        }
    }

    public void addAllBody(final String... contents) {
        addAllBody(true, contents);
    }

    public void addAllBody(final boolean destroyAll, final String... contents) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final String[] FINAL_CONTENTS;
                FINAL_CONTENTS = contents;
                addAllBodyInternal(destroyAll, FINAL_CONTENTS);
            }
        };

        runAfterOnlayout = runnable;
        if (widgetLength > 0) {
            runAfterOnlayout.run();
            runAfterOnlayout = null;
        }
    }

    /**
     * 添加所有数据
     *
     * @param contents
     */
    private void addAllBodyInternal(boolean destroyAll, String... contents) {
        if (destroyAll) {
            destroy();
        }
        int realWidth = (int) (widgetLength * (1 - SPACE_RETAINED_PERCENT));
        if (realWidth <= 0) {
            return;
        }

        //是否需要水平居中
        if (gravity >= 0) {
            scrollChildLayout.setGravity(gravity);
        } else {
            scrollChildLayout.setGravity(Gravity.LEFT);
        }

        boolean maxSpaceLimitAvailableFlag = true;
        int curLineNumber = 1;
        int totalCount = 0;
        int curCountInLine = 0;
        int curLength = 0;
        final int LINE_HEIGHT = AndroidSysUtil.PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP);
        ArrayList<String> curTextArr = new ArrayList<String>();
        for (int index = 0; index < contents.length; index++) {
            //行数限制和高度限制同时判断
            if ((curLineNumber > maxLines && maxLines > 0 && maxSpaceLimitAvailableFlag)
                    || (maxHeight > 0 && curLineNumber * LINE_HEIGHT > maxHeight && maxSpaceLimitAvailableFlag)) {
                //只询问一次
                maxSpaceLimitAvailableFlag = false;
                //有最大行数限制的话
                if (onDataFillingListener != null && onDataFillingListener.onReachMaxLine(totalCount)) {
                    //如果监听到处理了，直接返回
                    return;
                }

                //没处理的话，直接变成滚动的了（限制X行数据）
                scrollChildLayout.measure(MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED);
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = scrollChildLayout.getMeasuredHeight();
                setLayoutParams(params);

                if (!isAllowScroll) {
                    return;
                }
            }
            String curText = contents[index];
            int paddingWidth = curCountInLine == 0 ? 0 : AndroidSysUtil.PixelUtil.dp2px(context, BODY_HORIZONTAL_PADDING_BY_DIP);
            curLength += calcWidgetWidth(curText) + paddingWidth;
            if (curLength > realWidth) {
                //触发add单行
                if (curCountInLine > 0) {
                    index--;
                    curCountInLine = 0;
                    curLength = 0;
                    //这里因为要回退，所以isNeedFillEmpty强制为false
                    addBody(curTextArr.toArray(new String[0]));
                    curLineNumber++;
                    totalCount += curTextArr.size();
                    curTextArr.clear();
                } else {
                    //如果只有1个，就从了吧
                    addBody(curText);
                    curLineNumber++;
                    totalCount += 1;
                    curLength = 0;
                }
                continue;
            }

            curTextArr.add(contents[index]);
            curCountInLine++;

            //如果是最后一个，或者到达单行最大值
            if (index == contents.length - 1 || curCountInLine == MAX_CONTENT_COUNT) {
                addBody(curTextArr.toArray(new String[0]));
                totalCount += curTextArr.size();
                curLineNumber++;
                curCountInLine = 0;
                curLength = 0;
                curTextArr.clear();

                if (onDataFillingListener != null && index == contents.length - 1) {
                    onDataFillingListener.onDataFillDone();
                }
            }
        }

        setFocusableInternal(this);
    }

    /**
     * 添加一整行数据
     *
     * @param contents
     */
    private void addBody(String... contents) {
        if (contents == null || contents.length == 0) {
            return;
        }

        LinearLayout singleLine = new LinearLayout(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                AndroidSysUtil.PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP));
        singleLine.setLayoutParams(params);
        singleLine.setOrientation(LinearLayout.HORIZONTAL);
        singleLine.setGravity(Gravity.CENTER_VERTICAL);

        for (int index = 0; index < contents.length; index++) {
            TextView bodyView = new TextView(context);
            LinearLayout.LayoutParams nodeParams = new LinearLayout.LayoutParams(calcWidgetWidth(contents[index]),
                    AndroidSysUtil.PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP - SINGLE_LINE_HEIGHT_PADDING_VERTICAL_BY_DIP * 2));
            bodyView.setGravity(Gravity.CENTER);
            bodyView.setText(contents[index]);
            bodyView.setTextSize(BODY_SIZE_BY_SP);
            bodyView.setTextColor(BODY_COLOR);

            if (TextUtils.isEmpty(contents[index])) {
                bodyView.setVisibility(View.GONE);
            }

            if (index != 0) {
                nodeParams.setMargins(AndroidSysUtil.PixelUtil.dp2px(context, BODY_HORIZONTAL_PADDING_BY_DIP), 0, 0, 0);
            }

            bodyView.setLayoutParams(nodeParams);
            singleLine.addView(bodyView);
            //添加到内容管理器里
            createOrUpdateBodyEntity(contents[index], bodyView);
        }

        scrollChildLayout.addView(singleLine);
    }

    private BodyEntity createOrUpdateBodyEntity(String content, TextView view) {
        if (!TextUtils.isEmpty(content)) {
            for (int index = 0; index < list.size(); index++) {
                BodyEntity entity = list.get(index);
                if (!TextUtils.isEmpty(entity.content) && entity.content.equals(content)) {
                    //激活并更新节点
                    entity.isAlive = true;
                    entity.content = content;
                    entity.textView = view;
                    entity.setListener();
                    return entity;
                }
            }
        }

        //如果没有 就NEW一个
        BodyEntity entity = new BodyEntity(content, view);
        list.add(entity);
        changeViewState(entity, entity.isSelected);
        return entity;
    }

    /**
     * 获取标准的加号按钮宽度，以一行5个按钮，每个按钮俩字来计算，为了美观
     *
     * @return
     */
    private int getStandardButtonWidth() {
        if (addFlagWidgetWidth != 0) {
            return addFlagWidgetWidth;
        }
        int deviceWidth = AndroidSysUtil.getDeviceWidth(context);
        int spacePx = AndroidSysUtil.PixelUtil.dp2px(context, (MAX_CONTENT_COUNT - 1) * BODY_HORIZONTAL_PADDING_BY_DIP);
        int addFlagWidth = (deviceWidth - spacePx) / MAX_CONTENT_COUNT;
        addFlagWidgetWidth = addFlagWidth;
        return addFlagWidth;
    }

    /**
     * 计算控件宽度
     *
     * @return
     */
    private int calcWidgetWidth(String str) {
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(AndroidSysUtil.PixelUtil.sp2px(context, BODY_SIZE_BY_SP));
        }

        float pixel = paint.measureText(str) + AndroidSysUtil.PixelUtil.dp2px(context, 8)
                //是否支持.9自动扩展
                + AndroidSysUtil.PixelUtil.dp2px(context, extWidthDp);
        if (pixel < getStandardButtonWidth()) {
            return getStandardButtonWidth();
        } else {
            return (int) (pixel + 4);
        }
    }

    /**
     * 计算权重
     *
     * @param resultArray
     */
    private void calcWeight(float[] resultArray, String[] contents, boolean isSameWidth, boolean isNeedFillEmpty) {
        if (isSameWidth) {
            for (int index = 0; index < resultArray.length; index++) {
                resultArray[index] = 1;
            }
            return;
        }

        int maxLength = 0;
        for (int index = 0; index < contents.length; index++) {
            if (contents[index].length() > maxLength) {
                maxLength = contents[index].length();
            }
        }

        for (int index = 0; index < contents.length; index++) {
            resultArray[index] = contents[index].length();

            if (isNeedFillEmpty) {
                resultArray[index] = resultArray[index] == 0 ? AUTO_FILL_WEIGHT : resultArray[index];
            }
            if (SWITCHER_AUTO_BALANCE) {
                float min = maxLength / MAX_RATIO_BETWEEN_LARGEST_AND_SMALLEST;
                if (resultArray[index] < min) {
                    //自动调平衡
                    float waitToAdd = min - resultArray[index];
                    waitToAdd = waitToAdd / MAX_RATIO_BETWEEN_LARGEST_AND_SMALLEST;
                    resultArray[index] += waitToAdd;
                }
            }
        }
    }

    /**
     * 刷新整个布局
     */
    public void refreshView() {
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            boolean isSelected = entity.isSelected;
            changeViewState(entity, isSelected);
        }
    }

    /**
     * 改变视图状态
     *
     * @param entity
     * @param isSelected
     */
    private void changeViewState(BodyEntity entity, boolean isSelected) {
        entity.isSelected = isSelected;
    }

    private ArrayList<String> bodyArrToStringArr() {
        return bodyArrToStringArr(null);
    }

    private ArrayList<String> bodyArrToStringArr(String removedContent) {
        ArrayList<String> dstList = new ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if (!TextUtils.isEmpty(removedContent) && entity.content.equals(removedContent)) {
                continue;
            }
            if (!TextUtils.isEmpty(entity.content)) {
                dstList.add(entity.content);
            }
        }
        return dstList;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        widgetLength = r - l;
        if (maxHeight == HEIGHT_MATCH_PARENT) {
            widgetHeight = maxHeight = b - t;
        }

        if (runAfterOnlayout != null) {
            runAfterOnlayout.run();
            runAfterOnlayout = null;
        }
        if (runAfterOnlayoutForPreSelection != null) {
            runAfterOnlayoutForPreSelection.run();
            runAfterOnlayoutForPreSelection = null;
        }
    }

    public interface OuterBodyClickListener {
        void onClick(BodyEntity body);
    }

    /**
     * 数据到达最大行数的监听器
     */
    public interface OnDataFillingListener {
        /**
         * 数据到达最大行数
         *
         * @param dataTotalCount
         * @return true 构建停止 false 构建继续
         */
        boolean onReachMaxLine(int dataTotalCount);

        /**
         * 处理完成，并没有遇到maxLine或者没设置maxLine
         */
        void onDataFillDone();
    }

    private double dragScale = 1.2D;
    private ImageView dragImageView;

    private void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /**
     * 开始拖拽
     *
     * @param dragBitmap
     * @param x
     * @param y
     */
    public void startDrag(Bitmap dragBitmap, int x, int y) {
        //先清一下
        stopDrag();
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowParams.x = x;
        windowParams.y = y;

        windowParams.width = (int) (dragScale * dragBitmap.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
        windowParams.height = (int) (dragScale * dragBitmap.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
        this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        this.windowParams.format = PixelFormat.TRANSLUCENT;
        this.windowParams.windowAnimations = 0;
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(dragBitmap);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
        windowManager.addView(iv, windowParams);
        dragImageView = iv;
    }

    /**
     * 用于管理视图显示和获取当前选中项的实体类
     */
    public class BodyEntity {
        private boolean isSelected = false;
        private boolean isAlive = true;
        private String content = "";
        private boolean isAllowMoving;
        //被选的序号
        private int selectNumber = 0;

        private TextView textView;

        public BodyEntity(String content, TextView view) {
            this.textView = view;
            setListener();
        }

        public void setListener() {
            if (isAllowLongClick) {
                textView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mVibrator.vibrate(50);//设置震动时间
                        isAllowMoving = true;
                        return true;
                    }
                });
                textView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (isAllowMoving) {
                            textView.destroyDrawingCache();
                            textView.setDrawingCacheEnabled(true);
                            //textView.setVisibility(View.INVISIBLE);
                            Bitmap dragBitmap = Bitmap.createBitmap(textView.getDrawingCache());
                            startDrag(dragBitmap, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                            requestDisallowInterceptTouchEvent(true);
                        }

                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            isAllowMoving = false;
                        }
                        return false;
                    }
                });
            }
        }
    }
}
