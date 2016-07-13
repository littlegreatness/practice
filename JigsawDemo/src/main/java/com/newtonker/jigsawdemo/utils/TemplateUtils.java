package com.newtonker.jigsawdemo.utils;

import android.content.Context;

import com.newtonker.jigsawdemo.model.JigsawType;
import com.newtonker.jigsawdemo.model.TemplateEntity;
import com.newtonker.jigsawdemo.widget.JigsawModelLayout;

import java.util.ArrayList;
import java.util.List;

public class TemplateUtils {
    /**
     * 获取对应type类型的拼图模板
     *
     * @param context
     * @param type
     * @param paths
     * @return
     */
    public static List<JigsawModelLayout> getSlotLayoutList(Context context, JigsawType type, List<String> paths) {
        if (null == type || null == paths) {
            return null;
        }

        List<TemplateEntity> entityList = ParserHelper.getInstance(context).getEntityList(type);
        List<JigsawModelLayout> jigsawModelLayoutList = new ArrayList<>();

        // 这里的取值130和activity_select_photo.xml中的model_area高度有关；
        int width = DisplayUtils.dp2px(context, 130);
        int height = DisplayUtils.dp2px(context, 130);

        for (TemplateEntity entity : entityList) {
            JigsawModelLayout jigsawModelLayout = new JigsawModelLayout(context, false);
            jigsawModelLayout.setImagePathList(paths);
            jigsawModelLayout.setTemplateEntity(entity);
            // 这里必须要设置一个宽高值才能显示出图片
            jigsawModelLayout.reDraw(width, height);

            jigsawModelLayoutList.add(jigsawModelLayout);
        }

        return jigsawModelLayoutList;
    }

    /**
     * 获取单个SlotView的Entity
     *
     * @param context
     * @param type
     * @param position
     * @return
     */
    public static TemplateEntity getEntity(Context context, JigsawType type, int position) {
        if (null == type) {
            return null;
        }

        List<TemplateEntity> entities = ParserHelper.getInstance(context).getEntityList(type);
        if (position < 0 || position >= entities.size()) {
            return null;
        }

        return entities.get(position);
    }

    /**
     * 根据size大小判断JigsawType值
     *
     * @param size
     * @return
     */
    public static JigsawType getJigsawType(int size) {
        JigsawType jigsawType = null;
        switch (size) {
            case 1:
                jigsawType = JigsawType.ONE_PHOTO;
                break;
            case 2:
                jigsawType = JigsawType.TWO_PHOTO;
                break;
            case 3:
                jigsawType = JigsawType.THREE_PHOTO;
                break;
            case 4:
                jigsawType = JigsawType.FOUR_PHOTO;
                break;
            case 5:
                jigsawType = JigsawType.FIVE_PHOTO;
                break;
            default:
                break;
        }

        return jigsawType;
    }

}
