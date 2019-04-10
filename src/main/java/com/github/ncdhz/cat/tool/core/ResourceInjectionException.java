package com.github.ncdhz.cat.tool.core;

/**
 * @author majunlong
 * 资源注入异常
 * 在资源注入出现死锁时抛出
 */
public class ResourceInjectionException extends Exception {

    public ResourceInjectionException(String msg) {
        super(msg);
    }
}
