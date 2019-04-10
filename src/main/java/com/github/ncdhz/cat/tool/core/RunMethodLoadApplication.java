package com.github.ncdhz.cat.tool.core;


import com.github.ncdhz.cat.tool.annotation.RunMethod;
import com.github.ncdhz.cat.tool.util.Chopsticks;
import com.github.ncdhz.cat.tool.util.ChopsticksInitException;
import com.github.ncdhz.cat.tool.util.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @author majunlong
 * RunMethod 注解的加载器
 * 此类会实现怎么去加载 RunMethod
 */
public class RunMethodLoadApplication implements LoadApplication {

    private static Chopsticks chopsticks;
    private static Logger logger = LoggerFactory.getLogger(LoadApplication.class);
    static {
        try {
            chopsticks = Chopsticks.getChopsticks();
        } catch (ChopsticksInitException e) {
            logger.error("[{}] chopsticks init exception",RunMethodLoadApplication.class);
            e.printStackTrace();
        }
    }


    @Override
    public void run(Class<?> clazz) throws Exception {
        loadRunMethod(clazz);
    }

    /**
     * 运行没有参数并且含有 @RunMethod 注解的方法的具体实现
     */
    private void loadRunMethod(Class<?> clazz)throws Exception{
        ClassLoader classLoader = clazz.getClassLoader();
        Iterator<String> iterator = FilePath.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            Class<?> aClass = classLoader.loadClass(next);
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                RunMethod runMethod = method.getAnnotation(RunMethod.class);
                if (runMethod!=null){
                    Class<?>[] classes = method.getParameterTypes();
                    if (classes.length > 0){
                        throw new ParametersQuantityException("["+RunMethodLoadApplication.class+"] Too many parameters in '@RunMethod' annotation");
                    }
                    Object bean = chopsticks.getBean(aClass.getTypeName());
                    if (bean==null){
                        bean = chopsticks.getBean(aClass);
                    }
                    method.setAccessible(true);
                    method.invoke(bean);
                }
            }
        }

    }
}
