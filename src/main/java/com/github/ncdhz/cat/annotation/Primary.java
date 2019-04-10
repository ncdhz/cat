package com.github.ncdhz.cat.annotation;

import java.lang.annotation.*;

/**
 * 含有此注解的bean会优先被加载
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Primary {
}
