package com.github.ncdhz.cat.core;

/**
 * @author majunlong
 * 注解加载程序的接口
 */
public interface LoadApplication {
    /**
     *
     * @param clazz 需要传待处理类
     */
    void run(Class<?> clazz) throws Exception;
}
