package com.github.ncdhz.cat.tool.core;

/**
 * 初始化不了就会抛此异常
 * 方法参数和注入类不匹配（多参数，少参数，参数类型不对）
 */
public class ParametersQuantityException extends Exception{

    public ParametersQuantityException(String msg) {
        super(msg);
    }
}
