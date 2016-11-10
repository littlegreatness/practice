package com.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author：buxiaoqing on 2016/11/9 19:01
 * Just do IT(没有梦想,何必远方)
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PrintMe {
    int value() default 9999;
}
