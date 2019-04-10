package com.github.ncdhz.cat.tool.util;


import com.github.ncdhz.cat.tool.core.BeanInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author majunlong
 */
public class Tableware extends Chopsticks implements Iterable{

    private static Logger logger = LoggerFactory.getLogger(Chopsticks.class);

    private final static String ALIAS_OPEN ="true";

    private List<Food> bowlFoods = new ArrayList<>();

    @Override
    public Iterator<Food> iterator() {
        return bowlFoods.iterator();
    }

    @Override
    public Object getBeanByClassPath(String classPath) {
        if (classPath==null||"".equals(classPath)){
            return null;
        }
        Iterator<Food> iterator = iterator();
        while (iterator.hasNext()){
            Food food = iterator.next();
            if (classPath.equals(food.getClassPath())){
                return food.getObj();
            }
        }
        return null;
    }

    /**
     * 根据 class 类型返回一个对象
     * 现在 cat tool 里面获取
     * 如果没有获取到 新建一个 对象返回
     */
    @Override
    public <T> T getBean(Class<T> clazz){
        if (clazz==null){
            logger.error("Parameters cannot be null");
            throw new BeanInitException("Parameters cannot be null");
        }
        Object object = getBeanByInterface(clazz);
        if (object==null){
            logger.error("[{}] Error creating new instance",clazz);
            throw new BeanInitException("Class cannot be initialized"+" '"+
                    clazz+"'"+" You can add '@Component' annotation");
        }
        return (T) object;
    }

    /**
     * 如果输入的Class是接口
     * 那么在cat tool的容器中找到他的子类
     * 并返回  Primary 属性为 true 优先
     * 如果找完找到几个 并且没有一个 Primary 属性为正 则返回第一个
     * 没有找到返回 null
     */
    private Object getBeanByInterface(Class<?> clazz){
        Iterator<Food> iterator = iterator();
        Object object = null;
        while (iterator.hasNext()){
            Food next = iterator.next();
            Object obj = next.getObj();
            if (clazz.isAssignableFrom(obj.getClass())){
                if (next.isPrimary()){
                    return obj;
                }
                if (object==null){
                    object = obj;
                }
            }
        }
        return object;
    }

    /**
     * 对外提供的 根据名字获取Bean的方法
     */
    @Override
    public <T> T getBean(String name) {
        if (name==null||"".equals(name)){
            return null;
        }
        return (T) getBeanByBowl(name);
    }

    @Override
    public <T> T getNewBean(Class<T> clazz) {
        Object bean = getBean(clazz);
        Object oneObj = getNewObj(bean);
        return (T) oneObj;
    }

    @Override
    public Object getNewBeanByClassPath(String classPath) {
        Object bean = getBeanByClassPath(classPath);
        if (bean==null){
            return null;
        }
        Object oneObj = getNewObj(bean);
        return oneObj;
    }

    @Override
    public <T> T getNewBean(String name) {
        Object bean = getBean(name);
        if (bean==null){
            return null;
        }
        Object oneObj = getNewObj(bean);
        return (T) oneObj;
    }
    private Object getNewObj(Object object){
        Class<?> aClass = object.getClass();
        Object oneObj = getOneObj(aClass);
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Object fieldObj = field.get(object);
                Field declaredField = oneObj.getClass().getDeclaredField(field.getName());
                declaredField.setAccessible(true);
                declaredField.set(oneObj,fieldObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return oneObj;
    }

    private <T> T getOneObj(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new ChopsticksInitException("["+clazz+"] Reflex exception");
        }
    }


    /**
     * 根据name用于在bowlFoods容器中获取对象
     * 如果没有找到对象返回空
     */
    private Object getBeanByBowl(String name){
        Iterator<Food> iterator = iterator();
        Object object = null;
        while (iterator.hasNext()){
            Food next = iterator.next();
            List<String> names = next.getName();
            if (comName(name,names.toArray(new String[0]))){
                if (next.isPrimary()){
                    return next.getObj();
                }
                if (object==null){
                    object = next.getObj();
                }
            }
        }
        return object;
    }

    private boolean comName(String name,String ... names){
        boolean aliasOpen = aliasOpen();
        for (String n : names) {
            if (aliasOpen){
                if (n.equals(name.toUpperCase())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 通过类路径在cat tool容器中查询是否存在这个对象
     * 如果cat tool中存在这个对象则返回 true 否则 返回 false
     */
    @Override
    public boolean isExist(String classPath) {
        if (classPath==null||"".equals(classPath)){
            return false;
        }
        Iterator<Food> iterator = iterator();
        while (iterator.hasNext()){
            Food food = iterator.next();
            if (classPath.equals(food.getClassPath())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addFood(Object obj) {
        addFood(obj,false,obj.getClass().getSimpleName());
    }

    @Override
    public void addFood(Object obj, boolean primary) {
        addFood(obj,true,obj.getClass().getSimpleName());
    }

    @Override
    public void addFood(Object obj, String... name) {
        addFood(obj,false, name);
    }

    @Override
    public void addFood(Object obj, boolean primary, String... name) {
        if(!isExist(obj.getClass().getName())){
            if (aliasOpen()){
                for (int i=0;i<name.length;i++) {
                    name[i] = name[i].toUpperCase();
                }
            }
            bowlFoods.add(new Food(obj,primary,name));

        }
    }

    /**
     * 判断别名是否开启了
     */
    private static boolean aliasOpen(){
        String aliasOpen = System.getProperty("util.alias.open");
        if (aliasOpen==null){
            aliasOpen = "true";
            System.setProperty("util.alias.open",aliasOpen);
        }
        if (ALIAS_OPEN.equals(aliasOpen)){
            return true;
        }
        return false;
    }


}