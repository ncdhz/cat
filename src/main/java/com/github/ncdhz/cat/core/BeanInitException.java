package com.github.ncdhz.cat.core;

/**
 * @author majunlong
 * 在类初始化错误时抛出
 */
public class BeanInitException extends RuntimeException{

    public BeanInitException(String msg) {
        super(msg);
    }
}
