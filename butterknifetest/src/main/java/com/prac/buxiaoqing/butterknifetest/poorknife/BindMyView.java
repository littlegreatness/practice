package com.prac.buxiaoqing.butterknifetest.poorknife;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author：buxiaoqing on 2016/11/9 10:57
 * Just do IT(没有梦想,何必远方)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface BindMyView {
    //资源ID
    int value();
}
