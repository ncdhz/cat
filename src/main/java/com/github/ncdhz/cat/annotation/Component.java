package com.github.ncdhz.cat.annotation;

import java.lang.annotation.*;


/**
 * 用于把类加入cat ioc 容器中
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

}
