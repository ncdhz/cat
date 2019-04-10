package com.github.ncdhz.cat.core;

/**
 * 注解bean 注解的方法没有返回值
 * 注解bean 的方法必须有返回值
 */
public class BeanNoReturnException extends Exception {

    public BeanNoReturnException(String msg) {
        super(msg);
    }
}
