package com.github.ncdhz.cat.tool.annotation;


import java.lang.annotation.*;


/**
 * 此注解只能用在加载工具主类的类上
 * 别的类将没有效果
 * 此注解path对应的路径下面的类将被扫描
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScanPath {
    String[] paths();
}
