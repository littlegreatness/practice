package com.prac.buxiaoqing.myknifelibrary;

import android.app.Activity;

import java.lang.reflect.Method;

/**
 * author：buxiaoqing on 2016/11/14 11:17
 * Just do IT(没有梦想,何必远方)
 */
public final class MyKnife {


    public static void bindMyKnife(Activity activity) {
        createBinding(activity);
    }

    private static void createBinding(Activity activity) {
        Class<?> targetClass = activity.getClass();

        String name = targetClass.getSimpleName();
        try {
            String className = targetClass.getPackage().getName() + ".My_" + name + "AutoGenerate";
            Class<?> aClass = Class.forName(className);
            Method[] methods = aClass.getDeclaredMethods();
            for (Method md : methods) {
                System.out.println("Printing : " + md.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
