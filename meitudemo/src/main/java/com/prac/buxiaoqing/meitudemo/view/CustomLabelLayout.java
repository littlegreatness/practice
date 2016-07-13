package com.prac.buxiaoqing.meitudemo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tixa.core.R;
import com.tixa.util.AndroidSysUtil;
import com.tixa.util.PixelUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义可扩展布局
 * @author linfangxing
 *
 */
public class CustomLabelLayout extends ScrollView{
    //嗷嗷多的参数，如果调细节距离什么的，调这里
    public static final int TITLE_SIZE_BY_SP = 18;
    public static final int BODY_SIZE_BY_SP = 15;
    public static final int TITLE_COLOR = Color.BLACK;
    public int BODY_COLOR = Color.BLACK;
    public int BODY_COLOR_SELECTED = Color.WHITE;
    public int BODY_BACKGROUND_RESUORCE = R.drawable.cus_label_round_corner_stroke_bg_normal;
    public int BODY_BACKGROUND_SELECTED_RESUORCE = R.drawable.cus_label_round_corner_solid_bg_selected;
    public static final int BODY_BACKGROUND_ADD_FLAG_RESUORCE = R.drawable.public_custom_label_layout_body_add_flag;
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

    private boolean isAddFlagNeedShown = true;
    private boolean isAllowClick = true;
    private boolean isFocusable = true;
    //到达最大限制之后是否允许滚动
    private boolean isAllowScroll = false;
    //是否添加完就选中
    private boolean changeToSelectedWhenAppend = false;
    //可以指定加号用哪个布局
    private TextView specifiedAddFlagView;
    //是否需要水平居中？
//    private boolean needCenterHorizontal = false;
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
    //加号的特殊标志
    private static final String STR_ADD_FLAG = "[**加号**]";

    private Context context;

    private String title;
    private LinearLayout scrollChildLayout;

    private OnClickListener listener;
    private OuterBodyClickListener outerBodyListener;
    private OnDataFillingListener onDataFillingListener;

    private ArrayList<BodyEntity> list = new ArrayList<BodyEntity>();
    private ArrayList<BodyEntity> selectedList = new ArrayList<>();

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

    private void init(Context context, AttributeSet attrs, int defStyle){
        scrollChildLayout = new LinearLayout(context);
        scrollChildLayout.setOrientation(LinearLayout.VERTICAL);

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        scrollChildLayout.setLayoutParams(params);

        int paddingPx = PixelUtil.dp2px(context, PADDING_BY_DIP);
        setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx);

        addView(scrollChildLayout);
        setVerticalScrollBarEnabled(false);
    }

    /**
     * 添加标题项
     * @param title
     */
    public void addTitle(String title){
        if(TextUtils.isEmpty(title)){
            return;
        }

        if(!TextUtils.isEmpty(this.title)){
            //原来有标题了，移除
            scrollChildLayout.removeViewAt(0);
        }
        this.title = title;

        TextView titleView = new TextView(context);
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP));
        titleView.setLayoutParams(params);
        titleView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        titleView.setText(title);
        titleView.setTextSize(TITLE_SIZE_BY_SP);
        titleView.setTextColor(TITLE_COLOR);

        scrollChildLayout.addView(titleView, 0);
    }

    public void removeTitle(){
        if(TextUtils.isEmpty(title)){
            return;
        }

        title = "";
        scrollChildLayout.removeViewAt(0);
    }

    /**
     * 更换背景主题
     * @param resourceId
     */
    public void changeThemeForBackground(int resourceId){
        setBackgroundResource(resourceId);
    }
    public void changeThemeForBackgroundColor(int colorInt){
        setBackgroundColor(colorInt);
    }

    /**
     * 更换文字主题 bg resid为0表示不修改
     *
     * @param colorIntForUnselected
     * @param colorIntForSelected
     */
    public void changeThemeForTextColor(int colorIntForSelected, int colorIntForUnselected,
                                        int bodyBgResForSelected, int bodyBgResForUnselected){
        BODY_COLOR = colorIntForUnselected;
        BODY_COLOR_SELECTED = colorIntForSelected;
        BODY_BACKGROUND_RESUORCE = bodyBgResForUnselected <= 0 ? BODY_BACKGROUND_RESUORCE : bodyBgResForUnselected;
        BODY_BACKGROUND_SELECTED_RESUORCE = bodyBgResForSelected <= 0 ? BODY_BACKGROUND_SELECTED_RESUORCE : bodyBgResForSelected;

//		refreshView();
        rebuildAll();
    }


    /**
     * 监听body点击事件，内部点击逻辑会正常执行，可设置点击/非点击为相同资源ID来实现屏蔽选择状态切换的功能。
     * 传null，则取消外部监听
     * 和添加按钮的监听区分开
     * @param outerBodyListener
     */
    public void setOuterBodyClickListener(OuterBodyClickListener outerBodyListener){
        this.outerBodyListener = outerBodyListener;
    }

    /**
     * 设置数据到达最大行数的监听器
     * @param onDataFillingListener
     */
    public void setOnDataFillingListener(OnDataFillingListener onDataFillingListener){
        this.onDataFillingListener = onDataFillingListener;
    }

    public void removeOnDataFillingListener(){
        this.onDataFillingListener = null;
    }

    public void setAddFlagOnClickListener(OnClickListener lis){
        listener = lis;
    }

    /**
     * 指定加号的布局样式
     * @param specifiedAddFlagView
     */
    public void setSpecifiedAddFlagView(TextView specifiedAddFlagView) {
        this.specifiedAddFlagView = specifiedAddFlagView;
    }

    /**
     * 设置是否添加节点后直接选中
     * @param changeToselectedWhenAppend
     */
    public void setChangeToSelectedWhenAppend(boolean changeToselectedWhenAppend) {
        this.changeToSelectedWhenAppend = changeToselectedWhenAppend;
    }

    /**
     * 设置是否允许点击，不允许的话，内部点击事件也不执行了
     * @param enableClick
     */
    public void setAllowClick(boolean enableClick){
        if(isAllowClick == enableClick){
            return;
        }

        isAllowClick = enableClick;

        //这里需要重新构建控件了
        rebuildAll();
    }

    /**
     * 到达最大限制之后是否允许滚动，如果能滚动，就会自动拦截触摸事件
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

    private void setFocusableInternal(View parent){
        parent.setFocusable(isFocusable);
        if(parent instanceof ViewGroup){
            int childCount = ((ViewGroup)parent).getChildCount();
            if(childCount == 0){
                return;
            }
            for (int index = 0; index < ((ViewGroup)parent).getChildCount(); index++) {
                View child = ((ViewGroup)parent).getChildAt(index);
                setFocusableInternal(child);
            }
        }else{
            return;
        }
    }

    /**
     * 设置加号是不是显示
     * @param isNeedShown
     */
    public void setAddFlagNeedShown(boolean isNeedShown){
        if(isAddFlagNeedShown == isNeedShown){
            return;
        }

        isAddFlagNeedShown = isNeedShown;

        //这里需要重新构建控件了
        rebuildAll();
    }

    private void rebuildAll(){
        if(widgetLength > 0){
            //这里需要重新构建控件了
            clearBodies();
            ArrayList<String> bodyList = bodyArrToStringArr();

            addAllBody(bodyList.toArray(new String [0]));

            //去掉不活动的body实体
            destroyBodies();
        }
    }

    /**
     * 设置特定Gravity
     */
    public void setChildGravity(int gravity){
        if(this.gravity == gravity){
            return;
        }

        this.gravity = gravity;
        rebuildAll();
    }

    /**
     * 设置最大行数
     * @param maxLines
     */
    public void setMaxLines(int maxLines){
        this.maxLines = maxLines;
    }

    /**
     * 设置最大高度
     * @param maxHeight 如果设置为{@link #HEIGHT_MATCH_PARENT}，控件高度自动变更为MATCH_PARENT
     */
    public void setMaxHeight(int maxHeight){
        this.maxHeight = maxHeight;
        if(maxHeight == HEIGHT_MATCH_PARENT){
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            setLayoutParams(params);
        }
    }

    /**
     * 指定控件宽度，为了不等View的OnLayout
     * @param length
     */
    public void setWidgetLength(int length){
        widgetLength = length;
    }

    public void deleteBody(BodyEntity body){
        if (body == null){
            return;
        }
        //这里需要重新构建控件了
        clearBodies();
        ArrayList<String> bodyList = bodyArrToStringArr(body.content);

        addAllBody(bodyList.toArray(new String[0]));

        //去掉不活动的body实体
        destroyBodies();
    }

    /**
     * 单独加一个button
     * @param content
     */
    public boolean appendBody(String content){
        if(checkHasSameContentBody(content)){
            return false;
        }
        //所有值钱的body都置成不活动的
        clearBodies();
        ArrayList<String> bodyList = bodyArrToStringArr();
        bodyList.add(content);

        addAllBody(false, bodyList.toArray(new String[0]));

        //去掉不活动的body实体
        destroyBodies();
        return true;
    }

    private void clearBodies(){
        scrollChildLayout.removeAllViews();
        selectedList.clear();
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            entity.isAlive = false;
        }
    }

    public void destroy(){
        clearBodies();
        destroyBodies();
        list.clear();
        selectedList.clear();
        runAfterOnlayout = null;
    }

    private void destroyBodies(){
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if(!entity.isAlive){
                list.remove(index);
                index--;
            }
        }
    }

    public void addAllBody(List<String> contents){
        String[] finalContents = contents.toArray(new String[0]);
        addAllBody(finalContents);
    }

    public void addAllBody(final String ... contents){
        addAllBody(true, contents);
    }

    public void addAllBody(final boolean destroyAll, final String ... contents){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final String[] FINAL_CONTENTS;
                if(isAddFlagNeedShown){
                    //加号的话默认加一个标志
                    FINAL_CONTENTS = Arrays.copyOf(contents, contents.length + 1);
                    FINAL_CONTENTS[contents.length] = STR_ADD_FLAG;
                }else{
                    FINAL_CONTENTS = contents;
                }

                addAllBodyInternal(destroyAll, FINAL_CONTENTS);
            }
        };

        runAfterOnlayout = runnable;
        if(widgetLength > 0){
            runAfterOnlayout.run();
            runAfterOnlayout = null;
        }
    }

    /**
     * 设置选择的所有实体
     * @param contents
     */
    public void setSelectedBodies(final String ... contents){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setSelectedBodiesInternal(contents);
            }
        };

        runAfterOnlayoutForPreSelection = runnable;
        if(widgetLength > 0 && runAfterOnlayout == null){
            runAfterOnlayoutForPreSelection.run();
            runAfterOnlayoutForPreSelection = null;
        }
    }

    private void setSelectedBodiesInternal(final String ... contents){
        if(contents == null){
            return;
        }

        clearSelection();
        for (int indexContents = 0; indexContents < contents.length; indexContents++) {
            for (int index = 0; index < list.size(); index++) {
                BodyEntity entity = list.get(index);
                if (entity.content.equals(contents[indexContents])) {
                    changeViewState(entity, true);
                    break;
                }
            }
        }
    }

    /**
     * 添加所有数据
     * @param contents
     */
    private void addAllBodyInternal(boolean destroyAll, String ... contents){
        if(destroyAll){
            destroy();
        }
        int realWidth = (int) (widgetLength * (1 - SPACE_RETAINED_PERCENT));
        if(realWidth <= 0){
            return;
        }

        //是否需要水平居中
        if(gravity >= 0){
            scrollChildLayout.setGravity(gravity);
        }else{
            scrollChildLayout.setGravity(Gravity.LEFT);
        }

        boolean maxSpaceLimitAvailableFlag = true;
        int curLineNumber = 1;
        int totalCount = 0;
        int curCountInLine = 0;
        int curLength = 0;
        //确定的每行宽度
        int confirmedLineWidth = 0;
        final int LINE_HEIGHT = PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP);
        ArrayList<String> curTextArr = new ArrayList<String>();
        for (int index = 0; index < contents.length; index++) {
            //行数限制和高度限制同时判断
            if((curLineNumber > maxLines && maxLines > 0 && maxSpaceLimitAvailableFlag)
                    || (maxHeight > 0 && curLineNumber * LINE_HEIGHT > maxHeight && maxSpaceLimitAvailableFlag)){
                //只询问一次
                maxSpaceLimitAvailableFlag = false;
                //有最大行数限制的话
                if(onDataFillingListener != null && onDataFillingListener.onReachMaxLine(totalCount)){
                    //如果监听到处理了，直接返回
                    return;
                }

                //没处理的话，直接变成滚动的了（限制X行数据）
                scrollChildLayout.measure(MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED);
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = scrollChildLayout.getMeasuredHeight();
                setLayoutParams(params);

                if(!isAllowScroll){
                    return;
                }
            }
            boolean isNeedFillEmpty = index == contents.length - 1;
            String curText = contents[index];
            int paddingWidth = curCountInLine == 0 ? 0 : PixelUtil.dp2px(context, BODY_HORIZONTAL_PADDING_BY_DIP);
            confirmedLineWidth = curLength;
            curLength += calcWidgetWidth(curText) + paddingWidth;
            if(curLength > realWidth){
                //触发add单行
                if(curCountInLine > 0){
                    index--;
                    curCountInLine = 0;
                    curLength = 0;
                    //这里因为要回退，所以isNeedFillEmpty强制为false
                    addBody(false, false, confirmedLineWidth, !destroyAll, curTextArr.toArray(new String[0]));
                    curLineNumber ++;
                    totalCount += curTextArr.size();
                    curTextArr.clear();
                }else{
                    //如果只有1个，就从了吧
                    addBody(false, isNeedFillEmpty, curLength, !destroyAll, curText);
                    curLineNumber ++;
                    totalCount += 1;
                    curLength = 0;
                }
                continue;
            }

            curTextArr.add(contents[index]);
            curCountInLine++;

            //如果是最后一个，或者到达单行最大值
            if(index == contents.length - 1 || curCountInLine == MAX_CONTENT_COUNT){
                addBody(false, isNeedFillEmpty, curLength, !destroyAll, curTextArr.toArray(new String[0]));
                totalCount += curTextArr.size();
                curLineNumber ++;
                curCountInLine = 0;
                curLength = 0;
                curTextArr.clear();

                if(onDataFillingListener != null && index == contents.length - 1){
                    onDataFillingListener.onDataFillDone();
                }
            }
        }

        setFocusableInternal(this);
    }

    /**
     * 添加一整行数据
     * @param isSameWidth 是否等宽，不等宽的话，将按照LinearLayout的权重分配
     * @param contents
     */
    private void addBody(boolean isSameWidth, boolean isNeedFillEmpty, int lineLength, boolean isAppendOperation, String ... contents){
        if(contents == null || contents.length == 0){
            return;
        }

        LinearLayout singleLine = new LinearLayout(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP));
        singleLine.setLayoutParams(params);
        singleLine.setOrientation(LinearLayout.HORIZONTAL);
        singleLine.setGravity(Gravity.CENTER_VERTICAL);

        int lastOneIndex = 0;

        if(isNeedFillEmpty){
            lastOneIndex = contents.length - 1;
        }

        for (int index = 0; index < contents.length; index++) {
            TextView bodyView = new TextView(context);
            LinearLayout.LayoutParams nodeParams = new LinearLayout.LayoutParams(calcWidgetWidth(contents[index]),
                    PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP - SINGLE_LINE_HEIGHT_PADDING_VERTICAL_BY_DIP * 2));
            bodyView.setGravity(Gravity.CENTER);
            bodyView.setText(contents[index]);
            bodyView.setTextSize(BODY_SIZE_BY_SP);
            bodyView.setTextColor(BODY_COLOR);
            bodyView.setBackgroundResource(BODY_BACKGROUND_RESUORCE);

            if(TextUtils.isEmpty(contents[index])){
                bodyView.setVisibility(View.GONE);
            }

            if(index != 0){
                nodeParams.setMargins(PixelUtil.dp2px(context, BODY_HORIZONTAL_PADDING_BY_DIP), 0, 0, 0);
            }

            bodyView.setLayoutParams(nodeParams);
            singleLine.addView(bodyView);

            boolean isAddFlag = index == lastOneIndex && isNeedFillEmpty && isAddFlagNeedShown;
            if(isAddFlag){
                //这里给加号按钮特殊处理一下
                if(specifiedAddFlagView == null){
                    //没有指定加号布局
                    bodyView.setVisibility(View.VISIBLE);
                    bodyView.setText("");
                    bodyView.setBackgroundResource(R.drawable.public_custom_label_layout_body_add_flag);

                    nodeParams.weight = 0;
                    nodeParams.width = getStandardButtonWidth();
                    bodyView.setLayoutParams(nodeParams);
                }else{
                    //指定了加号布局
                    singleLine.removeView(bodyView);
                    bodyView = specifiedAddFlagView;
                    if(bodyView.getParent() != null){
                        ((ViewGroup)bodyView.getParent()).removeView(bodyView);
                    }
                    MarginLayoutParams paramsSpecified = (MarginLayoutParams) bodyView.getLayoutParams();
                    paramsSpecified.setMargins(PixelUtil.dp2px(context, BODY_HORIZONTAL_PADDING_BY_DIP), 0, 0, 0);
                    bodyView.setLayoutParams(paramsSpecified);
                    singleLine.addView(bodyView);
                    bodyView.setVisibility(VISIBLE);
                }
            }

            //添加到内容管理器里
            BodyEntity entity = createOrUpdateBodyEntity(contents[index], false, isAppendOperation, isAddFlag, bodyView);

            if(isAddFlag) {
                entity.isAddFlag = true;
            }
        }

        scrollChildLayout.addView(singleLine);
    }

    private BodyEntity createOrUpdateBodyEntity(String content, boolean isEditable, boolean isAppendOperation, boolean isAddFlag, TextView view){
        if(!TextUtils.isEmpty(content)){
            for (int index = 0; index < list.size(); index++) {
                BodyEntity entity = list.get(index);
                if(!TextUtils.isEmpty(entity.content) && entity.content.equals(content)){
                    //激活并更新节点
                    entity.isAlive = true;
                    entity.content = content;
                    entity.isEditable = isEditable;
                    entity.textView = view;
                    entity.setListener();
                    if(!isAddFlag){
                        changeViewState(entity, entity.isSelected);
                    }
                    return entity;
                }
            }
        }

        //如果没有 就NEW一个
        BodyEntity entity = new BodyEntity(content, isEditable, view);
        if(isAppendOperation && changeToSelectedWhenAppend){
            entity.isSelected = true;
        }
        list.add(entity);
        if(!isAddFlag){
            changeViewState(entity, entity.isSelected);
        }
        return entity;
    }

    /**
     * 看看有没有重复标签
     * @return
     */
    private boolean checkHasSameContentBody(String content){
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if(!TextUtils.isEmpty(entity.content) && entity.content.equals(content)){
                return true;
            }
        }

        return false;
    }

    /**
     * 添加编辑框
     * @param hint
     */
    public void addEditableBody(String hint){
        LinearLayout singleLine = new LinearLayout(context);
        singleLine.setFocusable(true);
        singleLine.setFocusableInTouchMode(true);
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP));
        singleLine.setLayoutParams(params);
        singleLine.setOrientation(LinearLayout.HORIZONTAL);
        singleLine.setGravity(Gravity.CENTER_VERTICAL);

        EditText bodyView = new EditText(context);
        LayoutParams nodeParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                PixelUtil.dp2px(context, SINGLE_LINE_HEIGHT_BY_DIP - SINGLE_LINE_HEIGHT_PADDING_VERTICAL_BY_DIP * 2));
        bodyView.setGravity(Gravity.CENTER);
        bodyView.setText(hint);
        bodyView.setTextSize(BODY_SIZE_BY_SP);
        bodyView.setTextColor(BODY_COLOR);
        bodyView.setBackgroundResource(BODY_BACKGROUND_RESUORCE);
        bodyView.setSingleLine();
        bodyView.setLayoutParams(nodeParams);
        singleLine.addView(bodyView);

        //添加到内容管理器里
        BodyEntity entity = new BodyEntity(hint, true, bodyView);
        list.add(entity);

        scrollChildLayout.addView(singleLine);
    }

    /**
     * 获取标准的加号按钮宽度，以一行5个按钮，每个按钮俩字来计算，为了美观
     * @return
     */
    private int getStandardButtonWidth(){
        if(addFlagWidgetWidth != 0){
            return addFlagWidgetWidth;
        }
        int deviceWidth = AndroidSysUtil.getDeviceWidth(context);
        int spacePx = PixelUtil.dp2px(context, (MAX_CONTENT_COUNT - 1) * BODY_HORIZONTAL_PADDING_BY_DIP);
        int addFlagWidth = (deviceWidth - spacePx) / MAX_CONTENT_COUNT;
        addFlagWidgetWidth = addFlagWidth;
        return addFlagWidth;
    }

    /**
     * 计算控件宽度
     * @return
     */
    private int calcWidgetWidth(String str){
        if(STR_ADD_FLAG.equals(str) && specifiedAddFlagView != null){
            //如果是加号，且传了加号的宽度，返回加号的宽度
            specifiedAddFlagView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            return specifiedAddFlagView.getMeasuredWidth();
        }
        if(paint == null){
            paint = new Paint();
            paint.setTextSize(PixelUtil.sp2px(context, BODY_SIZE_BY_SP));
        }

        float pixel = paint.measureText(str) + PixelUtil.dp2px(context, 8)
                //是否支持.9自动扩展
                + PixelUtil.dp2px(context, extWidthDp);
        if(pixel < getStandardButtonWidth()){
            return getStandardButtonWidth();
        }else{
            return (int) (pixel + 4);
        }
    }

    /**
     * 计算权重
     * @param resultArray
     */
    private void calcWeight(float[] resultArray, String[] contents, boolean isSameWidth, boolean isNeedFillEmpty){
        if(isSameWidth){
            for (int index = 0; index < resultArray.length; index++) {
                resultArray[index] = 1;
            }
            return;
        }

        int maxLength = 0;
        for (int index = 0; index < contents.length; index++) {
            if(contents[index].length() > maxLength){
                maxLength = contents[index].length();
            }
        }

        for (int index = 0; index < contents.length; index++) {
            resultArray[index] = contents[index].length();

            if(isNeedFillEmpty){
                resultArray[index] = resultArray[index] == 0 ? AUTO_FILL_WEIGHT : resultArray[index];
            }
            if(SWITCHER_AUTO_BALANCE){
                float min = maxLength / MAX_RATIO_BETWEEN_LARGEST_AND_SMALLEST;
                if(resultArray[index] < min){
                    //自动调平衡
                    float waitToAdd = min - resultArray[index];
                    waitToAdd = waitToAdd / MAX_RATIO_BETWEEN_LARGEST_AND_SMALLEST;
                    resultArray[index] += waitToAdd;
                }
            }
        }
    }

    /**
     * 获取当前选中的
     * @return
     */
    public ArrayList<String> getCurrentSelection(){
        ArrayList<String> retList = new ArrayList<String>();
        for (int index = 0; index < selectedList.size(); index++) {
            BodyEntity entity = selectedList.get(index);
            retList.add(entity.content);
        }

        return retList;
    }

    /**
     * 获取当前标题
     * @return
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置最多选几个
     * @param count
     */
    public void setMaxSelectCount(int count){
        maxSelectCount = count;
    }

    /**
     * 取消选择
     */
    private void clearSelection(){
        selectedList.clear();
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            changeViewState(entity, false);
        }
    }

    /**
     * 获取选择了多少个
     * @return
     */
    private int getSelectedCount(){
        int count = 0;
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if(entity.isSelected){
                count++;
            }
        }

        return count;
    }

    /**
     * 取消除了编辑框之外的选中
     */
    private void clearSelectionExceptEditableTextView(){
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if(entity.isEditable){
                continue;
            }
            changeViewState(entity, false);
        }
    }

    /**
     * 为点9主题设置非内容区（左+右）的宽度，单位dp
     * @param extWidthDp 非内容区的宽度总和，左+右，单位DP
     */
    public void setCellExtWidthForPoint9Theme(int extWidthDp){
        this.extWidthDp = extWidthDp;
    }

    /**
     * 刷新整个布局
     */
    public void refreshView(){
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            boolean isSelected = entity.isSelected;
            changeViewState(entity, isSelected);
        }
    }

    /**
     * 改变视图状态
     * @param entity
     * @param isSelected
     */
    private void changeViewState(BodyEntity entity, boolean isSelected){
        if(entity.isAddFlag){
            return;
        }

        entity.isSelected = isSelected;
        if(isSelected){
            entity.getView().setTextColor(BODY_COLOR_SELECTED);
            entity.getView().setBackgroundResource(BODY_BACKGROUND_SELECTED_RESUORCE);
        }else{
            entity.getView().setTextColor(BODY_COLOR);
            entity.getView().setBackgroundResource(BODY_BACKGROUND_RESUORCE);
        }

        dealClickEntity(entity);

        if(entity.isEditable){
            //编辑框特殊处理一下
            boolean isClear = TextUtils.isEmpty(entity.getView().getText().toString());
            if(isClear){
                entity.getView().setText(entity.editTextFadeHint);
            }

            if(!entity.isSelected){
                entity.getView().clearFocus();

                InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(entity.getView().getWindowToken(), 0);
            }
        }
    }

    /**
     * 处理添加到选择列表里
     * @param entity
     */
    private synchronized void dealClickEntity(BodyEntity entity){
        if((selectedList == null || selectedList.isEmpty()) && !entity.isSelected){
            return;
        }
        String content = entity.getContent();
        for (int index = 0; index < selectedList.size(); index++) {
            BodyEntity node = selectedList.get(index);
            if(node.getContent().equals(content)){
                //如果选择列表里有
                if(!entity.isSelected){
                    selectedList.remove(index);
                }

                return;
            }
        }

        //列表里没有，如果是添加的话，加上去
        if(entity.isSelected){
            selectedList.add(entity);
        }
//        LoggerUtil.show(context, StrUtil.transListToStringSplitWithComma(getCurrentSelection()));
    }

    private ArrayList<String> bodyArrToStringArr(){
        return bodyArrToStringArr(null);
    }

    private ArrayList<String> bodyArrToStringArr(String removedContent){
        ArrayList<String> dstList = new ArrayList<String>();
        for (int index = 0; index < list.size(); index++) {
            BodyEntity entity = list.get(index);
            if(!TextUtils.isEmpty(removedContent) && entity.content.equals(removedContent)){
                continue;
            }
            if(!entity.isAddFlag && !TextUtils.isEmpty(entity.content)){
                dstList.add(entity.content);
            }
        }

        return dstList;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        widgetLength = r - l;
        if(maxHeight == HEIGHT_MATCH_PARENT){
            widgetHeight = maxHeight = b - t;
        }

        if(runAfterOnlayout != null){
            runAfterOnlayout.run();
            runAfterOnlayout = null;
        }
        if(runAfterOnlayoutForPreSelection != null){
            runAfterOnlayoutForPreSelection.run();
            runAfterOnlayoutForPreSelection = null;
        }
    };

    public interface OuterBodyClickListener{
        void onClick(BodyEntity body);
    }

    /**
     * 数据到达最大行数的监听器
     */
    public interface OnDataFillingListener {
        /**
         * 数据到达最大行数
         * @param dataTotalCount
         * @return true 构建停止 false 构建继续
         */
        boolean onReachMaxLine(int dataTotalCount);

        /**
         * 处理完成，并没有遇到maxLine或者没设置maxLine
         */
        void onDataFillDone();
    }

    /**
     * 用于管理视图显示和获取当前选中项的实体类
     * @author linfangxing
     *
     */

    public class BodyEntity{
        private boolean isSelected = false;
        private boolean isEditable = false;
        private boolean isAddFlag = false;
        private boolean isAlive = true;
        private String content = "";
        private String editTextFadeHint;
        //被选的序号
        private int selectNumber = 0;

        private TextView textView;
        private EditText editTextView;

        public BodyEntity(String content, boolean isEditable, TextView view){
            this.content = isEditable ? "" : content;
            this.editTextFadeHint = content;
            this.isEditable = isEditable;

            if(isEditable){
                this.editTextView = (EditText) view;
            }else{
                this.textView = view;
            }

            setListener();
        }

        /**
         * 获取绑定的视图
         * @return
         */
        private TextView getView(){
            if(!isEditable){
                return textView;
            }else{
                return editTextView;
            }
        }

        private void setListener(){
            if(!isEditable){
                if(isAllowClick){
                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dealClickTextView();
                        }
                    });
                }
            }else{
                //第一次的时候只有焦点获得，木有onclick事件
                editTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            dealClickEditTextView();
                        }
                    }
                });
                editTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dealClickEditTextView();
                    }
                });
                editTextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        content = s.toString();
                    }
                });
            }
        }

        /**
         * 处理textview的点击
         */
        private void dealClickTextView(){
            if(!isAllowClick){
                return;
            }

            if(isAddFlag){
                //点的是添加
//				Toast.makeText(context, "添加标签！", Toast.LENGTH_SHORT).show();
                if(listener != null){
                    listener.onClick(null);
                }
                return;
            }
            if(!isSelected && getSelectedCount() >= maxSelectCount){
                Toast.makeText(context, "最多选择" + maxSelectCount + "个标签", Toast.LENGTH_SHORT).show();
                return;
            }
            isSelected = !isSelected;
            changeViewState(BodyEntity.this, isSelected);

            if(outerBodyListener != null){
                outerBodyListener.onClick(this);
            }
        }

        /**
         * 处理Editview的点击
         */
        private void dealClickEditTextView(){
            if(isSelected || !isAllowClick){
                return;
            }

            clearSelectionExceptEditableTextView();
            isSelected = true;
            changeViewState(BodyEntity.this, isSelected);

            boolean isClear = editTextView.getText().toString().equals(editTextFadeHint);
            if(isClear){
                editTextView.setText("");
            }
        }

        public boolean isSelected() {
            return isSelected;
        }

        public String getContent() {
            return content;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isAllowScroll){
            return super.onTouchEvent(ev);
        }

        return false;
    }

    /**
     * 制造工厂
     * 用于构建多页标签
     */
    public static class BuildFactory{
        private static ArrayList<CustomLabelLayout> list = new ArrayList<>();
        private static int dataCount = 0;
        private static boolean lock = false;
        /**
         * 创建多页标签，通过返回值返回结果对象数组
         * @param contents
         * @param maxHeight 多标签控件构造器不可使用{@link #HEIGHT_MATCH_PARENT}
         * @return
         */
        public static ArrayList<CustomLabelLayout> createMultiPageLabel(Context context, int widgetLength, int maxLines, int maxHeight, final String ... contents){
            list = new ArrayList<>();
            dataCount = 0;
            lock = false;
            int maxCount = 0;

            widgetLength = widgetLength - PixelUtil.dp2px(context, PADDING_BY_DIP * 2);
            maxCount = dataCount = contents.length;
            while(dataCount > 0){
                if(lock){
                    continue;
                }
                if(dataCount <= 0){
                    break;
                }

                int startIndex = maxCount - dataCount;
                int endIndex = maxCount;
                String[] data = Arrays.copyOfRange(contents, startIndex, endIndex);
                createSingleLabel(context, maxLines, maxHeight, widgetLength, data);
            }

            ArrayList<CustomLabelLayout> dstList = new ArrayList<>();
            dstList.addAll(list);
            list = null;
            return dstList;
        }

        private static void createSingleLabel(Context context, int maxLines, int maxHeight, int widgetLength, String ... data){
            lock = true;
            final CustomLabelLayout customLabelLayout = new CustomLabelLayout(context);
            customLabelLayout.setMaxLines(maxLines);
            customLabelLayout.setMaxHeight(maxHeight);
            customLabelLayout.setWidgetLength(widgetLength);
            customLabelLayout.setOnDataFillingListener(new OnDataFillingListener() {
                @Override
                public boolean onReachMaxLine(int dataTotalCount) {
                    list.add(customLabelLayout);
                    dataCount -= dataTotalCount;
                    lock = false;
                    customLabelLayout.removeOnDataFillingListener();
                    return true;
                }

                @Override
                public void onDataFillDone() {
                    list.add(customLabelLayout);
                    dataCount = 0;
                    lock = false;
                    customLabelLayout.removeOnDataFillingListener();
                }
            });
            customLabelLayout.addAllBody(data);
        }
    }
}
