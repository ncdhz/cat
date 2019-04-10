package com.github.ncdhz.cat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * 文件类用于存储路径解析器解析出来的所有文件路径
 * @author majunlong
 */
public class FilePath{

    private static Logger logger = LoggerFactory.getLogger(FilePath.class);

    private final static String CLASS_END_NAME = ".class";

    private static Set<String> paths = new HashSet<String>();

    /**
     * 判断文件是不是类二进制文件 后缀为 .class
     */
    public static boolean isClassFile(String name){
        File file = new File(name);
        return isClassFile(file);
    }

    public static boolean isClassFile(File file){
        if (!file.isFile()){
            logger.warn("{} Is not a file",file.getPath());
            return false;
        }
        if (!file.getPath().endsWith(CLASS_END_NAME)){
            logger.warn("{} Is not a class",file.getPath());
            return false;
        }
        return true;
    }

    /**
     * 路径加载类指定的文件或者文件夹的路径会被此方法解析放入 paths
     * 方法采用非递归实现
     * @param path 文件或者文件夹路径
     */
    public static void loadPath(String path,String packagePath){
        File file = new File(path);
        if (file.isDirectory()){
            File[] pathList = file.listFiles();
            assert pathList != null;
            LinkedHashMap<File,String> linkedHashMap = new LinkedHashMap<>();
            for (File pathL : pathList) {
                linkedHashMap.put(pathL,packagePath);
            }
            LinkedList<File> linkedList = new LinkedList<>(Arrays.asList(pathList));
            while (!linkedList.isEmpty()){
                File file1 = linkedList.removeFirst();
                packagePath = linkedHashMap.get(file1);
                linkedHashMap.remove(file1);
                if(file1.isDirectory()){

                    File[] files = file1.listFiles();
                    packagePath = packagePath+"."+file1.getName();

                    assert files != null;
                    linkedList.addAll(Arrays.asList(files));
                    for (File file2 : files) {

                        linkedHashMap.put(file2,packagePath);
                    }
                }else {
                    setPath(file1,packagePath);
                }

            }
        }else {
            setPath(file,packagePath);
        }
    }
    /**
     * 添加数据到 paths
     */
    private static void setPath(File path,String packagePath){
        if(!isClassFile(path)&&!isClassFile(path.getPath()+CLASS_END_NAME)){
            logger.warn("[{}] Path error",path);
            return;
        }
        String classPath = packagePath+"."+path.getName().replace(CLASS_END_NAME,"");
        paths.add(classPath);
        logger.debug("[{}] Path Added Successfully",classPath);
    }

    public static Iterator<String> iterator() {
        return paths.iterator();
    }

}
