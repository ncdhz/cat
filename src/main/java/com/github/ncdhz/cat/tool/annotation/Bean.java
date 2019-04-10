package com.github.ncdhz.cat.tool.annotation;

import java.lang.annotation.*;

/**
 * 用在方法上
 * 保存返回值在cat ioc容器中
 * 没有返回值只运行方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    String[] name() default "";
}
