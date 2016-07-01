package com.prac.buxiaoqing.prac.gif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.prac.buxiaoqing.prac.gif.model.KeyWordAnimationNode;
import com.prac.buxiaoqing.prac.gif.util.PixelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by buxiaoqing on 16/6/24.
 */
public class AnimateView extends View {
    private Context context;

    public static final int SIZE_WIDTH_DP = 25;
    public static final int SIZE_HEIGHT_DP = 25;

    // /**小球集合**/
    private List<KeyWordAnimationNode> keyWordAnimationList = new ArrayList<KeyWordAnimationNode>();
    /**
     * 资源id
     **/
    private int resId;
    /**
     * 第一批个数
     **/
    private int animNodeCount = 30;

    /**
     * 屏幕宽度
     **/
    private int width = 0;

    private boolean isStart = false;

    public AnimateView(Context context, int resId) {
        super(context);
        this.context = context;
        this.resId = resId;
        init(context);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        build(animNodeCount, keyWordAnimationList);
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    /**
     * 创建花
     *
     * @param count                 表情的数量
     * @param keyWordAnimationNodes
     */
    private void build(int count, List<KeyWordAnimationNode> keyWordAnimationNodes) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            //屏幕顶端随机位置
            // int s = random.nextInt(max) % (max - min + 1) + min;
            //平分屏幕顶端
            int s = width / count * i;

            float startY = -(PixelUtil.dp2px(context, SIZE_HEIGHT_DP) / 15F) * (random.nextInt((int) (20F * 15)) + 30);
            KeyWordAnimationNode keyWordAnimationNode = new KeyWordAnimationNode();
            //随机一个速度
            float speedY = random.nextFloat() * 6 + 7;
            float speedX = random.nextFloat() * 2 - 1;
            keyWordAnimationNode.setSpeedY(speedY);
            keyWordAnimationNode.setSpeedX(speedX);
            keyWordAnimationNode.setY(startY);
            keyWordAnimationNode.setX(s);
            Drawable drawable = context.getResources().getDrawable(resId);
            keyWordAnimationNode.setDrawable(drawable);
            keyWordAnimationNodes.add(keyWordAnimationNode);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isStart) {
            return;
        }
        super.onDraw(canvas);
        drawAnimation(canvas, keyWordAnimationList);

        //FPS 50
        invalidate();
    }

    /**
     * @param canvas
     * @param keyWordAnimationNodes
     */
    private void drawAnimation(Canvas canvas, List<KeyWordAnimationNode> keyWordAnimationNodes) {
        for (int index = 0; index < keyWordAnimationNodes.size(); index++) {
            KeyWordAnimationNode keyWordAnimationNode = keyWordAnimationNodes.get(index);
            if (isOneAnimationEnd(keyWordAnimationNode)) {
                //这个完成使命了
                keyWordAnimationNodes.remove(index);
                index--;
                continue;
            }
            float targetX = keyWordAnimationNode.getX() + keyWordAnimationNode.getSpeedX();
            keyWordAnimationNode.setX(targetX);
            float targetY = keyWordAnimationNode.getY() + keyWordAnimationNode.getSpeedY();
            keyWordAnimationNode.setY(targetY);

            Drawable drawable = keyWordAnimationNode.getDrawable();
            drawable.setBounds(
                    (int) targetX,
                    (int) targetY,
                    (int) targetX + PixelUtil.dp2px(context, SIZE_WIDTH_DP),
                    (int) targetY + PixelUtil.dp2px(context, SIZE_HEIGHT_DP)
            );
            drawable.draw(canvas);
        }

        if (keyWordAnimationNodes.size() == 0) {
            destroy();
        }
    }

    private boolean isOneAnimationEnd(KeyWordAnimationNode keyWordAnimationNode) {
        if (keyWordAnimationNode.getY() > getHeight()
                || keyWordAnimationNode.getX() > getWidth()
                || keyWordAnimationNode.getX() < -PixelUtil.dp2px(context, SIZE_WIDTH_DP)) {
            //不见了
            return true;
        }

        return false;
    }

    public void startAnimation() {
        isStart = true;
        postInvalidate();
    }

    public void destroy() {
        keyWordAnimationList.clear();
        isStart = false;
    }
}
