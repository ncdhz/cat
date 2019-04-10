package com.github.ncdhz.cat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author majunlong
 * util.container 用于指定容器的类 默认为 com.github.ncdhz.cat.tool.util.Tableware
 */
public abstract class Chopsticks {

    /**
     * 根据类的名字从 cat tool 容器中寻找相同的类
     * @param classPath 需要寻找的类的路径
     * @return 返回找到的类
     */
    public abstract Object getBeanByClassPath(String classPath);

    /**
     * 根据传进来的参数在 cat tool 容器中寻找是否有相同的类
     * 没有找到会抛出运行时异常 BeanInitException
     * 返回的值是单例模式
     * @param clazz 对象的 class 属性
     * @return 找到的类 没有找到抛出运行时异常 BeanInitException
     */
    public abstract <T> T getBean(Class<T> clazz);

    /**
     * 更具传入的名字在 cat tool 容器中寻找是否有相同的类
     * 返回的值是单例模式
     * @param name 需要寻找的类的名字
     * @return 找到的类 没有找到返回空
     */
    public abstract <T> T getBean(String name);

    /**
     * 根据传进来的参数在 cat tool 容器中寻找是否有相同的类
     * 没有找到会抛出运行时异常 BeanInitException
     * 返回的值是多列 一个含有以前注入参数的全新对象
     * @param clazz 需要传入的class
     * @return 返回一个 new 的对象
     */
    public abstract <T> T getNewBean(Class<T> clazz);

    public abstract Object getNewBeanByClassPath(String classPath);

    public abstract <T> T getNewBean(String name);



    private static Chopsticks chopsticks = null;

    /**
     * 获取 Chopsticks 类
     * 默认 实现类 Tableware
     * 可以修改 util.container 属性来指定默认实现类
     */
    public static Chopsticks getChopsticks(){

        synchronized (Chopsticks.class){
            if (chopsticks==null){
                String container = System.getProperty("util.container");
                if (container==null){
                    container = "com.github.ncdhz.cat.tool.util.Tableware";
                    System.setProperty("util.container",container);
                }
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                try {
                    chopsticks = (Chopsticks)classLoader.loadClass(container).newInstance();
                } catch (Exception e) {
                    throw new ChopsticksInitException("Chopsticks init err");
                }
            }
        }
        return chopsticks;
    }

    /**
     * 判断类路径是否在 cat tool 中存在
     * @param classPath
     * @return
     */
    public abstract boolean isExist(String classPath);

    public abstract void addFood(Object obj);

    public abstract void addFood(Object obj, boolean primary);

    public abstract void addFood(Object obj, String ... name);

    public abstract void addFood(Object obj, boolean primary, String ... name);

    class Food{

        private final static String CLASS_END_NAME = ".class";

        private List<String> name = new ArrayList<>();

        private Object obj;

        private boolean primary;

        private String classPath;

        List<String> getName() {
            return name;
        }

        Food(Object obj, boolean primary, String ... name) {
            this.obj = obj;
            this.primary = primary;
            this.classPath = obj.getClass().getName();
            setName(name);
        }

        void setName(String ... name) {
            this.name.addAll(Arrays.asList(name));
        }

        Object getObj(){
            return obj;
        }


        String getClassPath() {
            return classPath;
        }


        boolean isPrimary() {
            return primary;
        }

    }
}
