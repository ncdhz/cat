package com.github.ncdhz.cat.tool.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 方法运行注解
 * 含有此注解的方法会默认执行
 * 但是返回值不会被保存
 * 被此方法运行的方法必须没有返回值
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
@Documented
public @interface RunMethod {
}
