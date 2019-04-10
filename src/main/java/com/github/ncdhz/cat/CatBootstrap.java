package com.github.ncdhz.cat;

import com.github.ncdhz.cat.core.LoadApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具包的引导程序
 * @author majunlong
 */
public class CatBootstrap {


    private static Logger logger = LoggerFactory.getLogger(CatBootstrap.class);
    /**
     *  此方法会先加载  PathLoadApplication 用于整理所有需要处理的文件路径
     *  在加载 RunMethodLoadApplication 用于处理所有需要运行的方法
     *  ann.path.load 用于指定路径加载类的路径
     *  ann.run.method.load 用于指定方法运行加载类的路径
     *  ann.bean.load 用于指定bean加载类的路径
     */
    public static void run(Class<?> clazz){
        if (clazz==null){
            logger.error("Class cannot be null");
            throw new NullPointerException();
        }
        String pathLoad = System.getProperty("ann.path.load");
        if (pathLoad==null){
            pathLoad = "com.github.ncdhz.cat.core.PathLoadApplication";
            System.setProperty("ann.path.load",pathLoad);
        }
        String runMethod = System.getProperty("ann.run.method.load");
        if(runMethod==null){
            runMethod = "com.github.ncdhz.cat.core.RunMethodLoadApplication";
            System.setProperty("ann.run.method.load",runMethod);
        }
        String beanLoad = System.getProperty("ann.bean.load");
        if (beanLoad==null){
            beanLoad = "com.github.ncdhz.cat.core.BeanLoadApplication";
            System.setProperty("ann.bean.load",beanLoad);
        }
        ClassLoader classLoader = clazz.getClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try{
            long timeS = System.nanoTime();
            logger.debug("Start tool cat path loading");
            Class<?> pClass = classLoader.loadClass(pathLoad);
            LoadApplication pathLoadObj = (LoadApplication)pClass.newInstance();
            pathLoadObj.run(clazz);
            pathLoadObj.run(CatBootstrap.class);
            logger.debug("Tool cat path loading end");
            logger.debug("Start tool cat bean loading");
            Class<?> bClass = classLoader.loadClass(beanLoad);
            LoadApplication  bClassObj = (LoadApplication)bClass.newInstance();
            bClassObj.run(clazz);
            logger.debug("Tool cat bean loading end");
            logger.debug("Start tool cat run method function loading");
            Class<?> rClass = classLoader.loadClass(runMethod);
            LoadApplication  rClassObj = (LoadApplication)rClass.newInstance();
            rClassObj.run(CatBootstrap.class);
            logger.debug("Tool cat run method function loading end");
            long timeE = System.nanoTime();
            logger.info("Tool cat startup takes {} nano time",timeE-timeS);
        }catch (Exception e){
            logger.error("Tool cat startup error");
            e.printStackTrace();
        }
    }
}
