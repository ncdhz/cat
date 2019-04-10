package com.github.ncdhz.cat.tool.core;


import com.github.ncdhz.cat.tool.annotation.Bean;
import com.github.ncdhz.cat.tool.annotation.Component;
import com.github.ncdhz.cat.tool.annotation.Primary;
import com.github.ncdhz.cat.tool.annotation.Resource;
import com.github.ncdhz.cat.tool.util.Chopsticks;
import com.github.ncdhz.cat.tool.util.ChopsticksInitException;
import com.github.ncdhz.cat.tool.util.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author majunlong
 */
public class AnnBeanLoadImp implements BeanLoad{


    private static Chopsticks chopsticks;

    /**
     * bean加载的方法的参数最大数量
     */
    private static final int MAX_PARAMETER_NUM = 1;

    /**
     * 方法返回值 void 的常量
     */
    private static final String METHOD_RETURN_VOID = "void";

    private static Logger logger = LoggerFactory.getLogger(BeanLoad.class);

    /**
     * 储存 resource 的中间件 没有解析的 resource 会存放在这里
     */
    private static LinkedList<ResourceContainer> methods = new LinkedList<>();



    /*
     * 加载 Chopsticks 类
     * Chopsticks 是 cat tool 中容器的操作接口
     * 里面定义了很多操作 cat tool 容器的方法
     */
    static {
        try {
            chopsticks = Chopsticks.getChopsticks();
        } catch (ChopsticksInitException e) {
            logger.error("[{}] chopsticks init exception",AnnBeanLoadImp.class);
            e.printStackTrace();
        }
    }



    @Override
    public void loadComponentAndPrimary(Class<?> clazz) throws Exception {
        ClassLoader classLoader = clazz.getClassLoader();
        Iterator<String> paths = FilePath.iterator();
        while (paths.hasNext()){
            String classPath = paths.next();
            Class<?> aClass = classLoader.loadClass(classPath);

            Component component = aClass.getAnnotation(Component.class);
            if (component!=null){
                Primary primary = aClass.getAnnotation(Primary.class);
                Object obj = aClass.newInstance();
                if (primary!=null){
                    chopsticks.addFood(obj,true,classPath);
                }else {
                    chopsticks.addFood(obj,classPath);
                }
            }
        }
    }

    @Override
    public void loadBeanAndPrimary(Class<?> clazz) throws Exception {
        ClassLoader classLoader = clazz.getClassLoader();
        Iterator<String> paths = FilePath.iterator();
        while (paths.hasNext()){
            String classPath = paths.next();
            Class<?> aClass = classLoader.loadClass(classPath);
            Method[] methods = aClass.getDeclaredMethods();
            Object obj = null;
            for (Method method : methods) {
                /*
                 * 查看方法上面是否有资源注入注解器
                 * 并判断注解器和方法是否对应
                 * 方法是否规范
                 * 方法不规范报 ResourceInjectionException
                 */
                Resource resource = method.getAnnotation(Resource.class);
                if (resource!=null){
                    /*
                     * 获取被 Resource 注解方法的参数
                     * 当参数个数不等于一时报错
                     */
                    Class<?>[] parameters = method.getParameterTypes();
                    if (parameters==null||parameters.length != MAX_PARAMETER_NUM){
                        throw new ParametersQuantityException("The number of resources does not match the parameters");
                    }
                    /*
                     * 添加方法的基本属性到 methods 容器中
                     */
                    AnnBeanLoadImp.methods.add(new ResourceContainer(aClass.getName(),method.getName(),parameters,resource.name()));

                }else {
                    /*
                     * 当不方法中不存在 Resource 注解时 判断是否存在 Bean 注解
                     * 如果有 Bean 注解会对注解进行解析 放到 cat tool 的类存储容器中
                     */
                    Bean bean = method.getAnnotation(Bean.class);
                    if (bean!=null){
                        Type type = method.getAnnotatedReturnType().getType();

                        if (METHOD_RETURN_VOID.equals(type.getTypeName())){
                            throw new BeanNoReturnException("["+aClass.getName()+"] '"+method.getName()+"' method no return value. Please add a return value");
                        }

                        if (obj==null){
                            obj = chopsticks.getBean(aClass);
                        }
                        method.setAccessible(true);
                        Object invoke = method.invoke(obj);
                        Primary primary = method.getAnnotation(Primary.class);
                        String[] name = addAlias(method.getName(),bean.name());

                        if (primary!=null){
                            chopsticks.addFood(invoke,true,name);
                        }else {
                            chopsticks.addFood(invoke,name);
                        }
                    }
                }

            }
        }
    }

    /**
     * 此方法可以把 name 和 name1 连接成一个数组
     * 并对数组中的字段去重
     */
    private String[] addAlias(String name,String ... name1){
        Set<String> names = new TreeSet<>();
        names.add(name);
        for (String n : name1) {
            if (n!=null&&!"".equals(n)){
                names.add(n);
            }
        }
        return names.toArray(new String[0]);
    }


    @Override
    public void loadResourceByBean(Class<?> clazz) throws Exception {
        ClassLoader classLoader = clazz.getClassLoader();
        Iterator<String> paths = FilePath.iterator();
        while (paths.hasNext()){
            String classPath = paths.next();
            Class<?> aClass = classLoader.loadClass(classPath);
            Object soureObj = null;
            Field[] fields = aClass.getDeclaredFields();
            /*
             * 获取 aClass 的字段
             * 并查看 aClass 的字段中是否含有Resource注解
             * 如果含有Resource注解会自动填充注解
             */
            for (Field field : fields) {
                Resource resource = field.getAnnotation(Resource.class);
                if (resource!=null){
                    String objName = resource.name();
                    Object object;
                    object = getBean(objName,field.getName());
                    if (object==null){
                        object = chopsticks.getBean(field.getType());
                    }
                    if (object==null){
                        continue;
                    }
                    if (soureObj==null){
                        soureObj = chopsticks.getBean(aClass);
                    }
                    field.setAccessible(true);
                    field.set(soureObj,object);
                }
            }
        }
        loadResourceMethodByBean(clazz);
    }
    /**
     * 对含有  Resource 注解的方法进行初始化
     * 如果方法中含有Bean 注解则加载方法并把方法的返回值添加到 cat tool 容器
     * 如果方法含有Bean 但是没有返回值 则  throw new BeanNoReturnException()
     */
    private void loadResourceMethodByBean(Class<?> clazz) throws Exception{
        ClassLoader classLoader = clazz.getClassLoader();
        LinkedList<ResourceContainer> methods = AnnBeanLoadImp.methods;
        int count = methods.size() * 2;
        method:while (!methods.isEmpty()){
            ResourceContainer rc = methods.removeFirst();
            count--;
            if (count<0){
                logger.error("[{}] Resource injection could not find a class in {}",rc.getMethodName(),rc.getClassPath());
                throw new ResourceInjectionException("Circular calls occur in resource injection '"+rc.getMethodName()+"' in "+rc.getClassPath());
            }
            Class<?> aClass = classLoader.loadClass(rc.getClassPath());
            Class<?>[] parameterTypes = rc.getParameterTypes();
            Method method = aClass.getDeclaredMethod(rc.getMethodName(), parameterTypes);
            Object[] objects = new Object[parameterTypes.length];
            for (int i=0;i<parameterTypes.length;i++) {
                Object bean = getBean(rc.getResourceName(), method.getName());
                if (bean==null){
                    try{
                        bean = chopsticks.getBean(parameterTypes[i]);
                    }catch (BeanInitException e){
                        logger.warn("[{}] Class not found",parameterTypes[i]);
                    }
                }
                if (bean==null){
                    /*
                     * 在cat tool容器中没有找到方法所需要的类
                     * 则把方法从新加入待处理集合
                     */
                    methods.add(rc);
                    continue method;
                }
                objects[i] = bean;
            }
            Object bean = chopsticks.getBean(aClass);
            if (bean==null){
                bean = aClass.newInstance();
                chopsticks.addFood(bean);
            }
            method.setAccessible(true);
            Object o = method.invoke(bean, objects);
            Bean beanAnn = method.getAnnotation(Bean.class);
            if (beanAnn!=null){
                if (METHOD_RETURN_VOID.equals(o.getClass().getTypeName())){
                    throw new BeanNoReturnException(method.getName()+" method no return value. Please add a return value");
                }
                Primary primary = method.getAnnotation(Primary.class);
                if (primary==null){
                    chopsticks.addFood(o,false);

                }else {
                    chopsticks.addFood(o,true);
                }
            }
        }
    }

    /**
     * 根据名字返回 一个对象
     * 对象会从cat tool 容器查找
     * 没有找到会返回空
     */
    private Object getBean(String ... name){
        Object object = null;
        for (String n : name) {
            if (n!=null&&!"".equals(n)) {
                object = chopsticks.getBean(n);
                break;
            }
        }
        return object;
    }
}
