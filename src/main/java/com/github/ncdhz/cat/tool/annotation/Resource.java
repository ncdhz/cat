package com.github.ncdhz.cat.tool.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 此方法为注入方法
 * 含有自动注入功能
 * 此方法也可以用在函数上面
 * 含有此方法的函数必须有一个参数
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface Resource {
    String name() default "";
}
