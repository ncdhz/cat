package com.github.ncdhz.cat.core;

/**
 * @author majunlong
 */
public class BeanLoadApplication implements LoadApplication {

    @Override
    public void run(Class<?> clazz) throws Exception {
        String beanLoadImp = System.getProperty("ann.bean.load.imp");
        if (beanLoadImp==null){
            beanLoadImp = "com.github.ncdhz.cat.tool.core.AnnBeanLoadImp";
            System.setProperty("ann.bean.load.imp",beanLoadImp);
        }
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?> bClass = classLoader.loadClass(beanLoadImp);
        BeanLoad bClassObj = (BeanLoad)bClass.newInstance();
        bClassObj.loadComponentAndPrimary(clazz);
        bClassObj.loadBeanAndPrimary(clazz);
        bClassObj.loadResourceByBean(clazz);
    }



}
