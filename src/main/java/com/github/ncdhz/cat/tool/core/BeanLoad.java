package com.github.ncdhz.cat.tool.core;

/**
 * bean 加载接口
 * @author majunlong
 */
public interface BeanLoad {

    /**
     * 用于解释Component 和 类上的Primary 注解
     */
    void loadComponentAndPrimary(Class<?> clazz) throws Exception;

    /**
     * 用于解释Bean 和 方法上的Primary 注解
     */
    void loadBeanAndPrimary(Class<?> clazz) throws Exception;

    /**
     * 注入 Resource 用已有的 bean
     */
    void loadResourceByBean(Class<?> clazz) throws Exception;

    /**
     * Resource 注入时出现bean没有加载完全
     * 用于临时存储 含有此注解的一些基本信息
     * 方便后面再次注入
     */
    class ResourceContainer{
        private String classPath;
        private String methodName;
        private Class<?>[] parameterTypes;
        private String resourceName;

        ResourceContainer(String classPath, String methodName, Class<?>[] parameterTypes, String resourceName) {
            this.classPath = classPath;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.resourceName = resourceName;
        }

        public String getResourceName() {
            return resourceName;
        }

        String getClassPath() {
            return classPath;
        }

        String getMethodName() {
            return methodName;
        }

        Class<?>[] getParameterTypes() {
            return parameterTypes;
        }
    }
}
