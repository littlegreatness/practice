package com.prac.buxiaoqing.butterknifetest.poorknife;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * author：buxiaoqing on 2016/11/9 10:59
 * Just do IT(没有梦想,何必远方)
 * <p>
 * 通过反射实现BindMyView
 */
public class PoorKnife {
    private static String tag = PoorKnife.class.getSimpleName();

    public static void injectMyView(Activity activity) {
        try {
            Class contextClass = Class.forName(activity.getClass().getCanonicalName());

            for (Field field : contextClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(BindMyView.class)) {
                    Log.d(tag, field.getName() + "  has annotation");

                    int id = field.getAnnotation(BindMyView.class).value();
                    String type = field.getType().toString();
                    //并没有实现自动转型
                    if (type.endsWith("TextView")) {
                        field.setAccessible(true);
                        field.set(activity, activity.findViewById(id));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
