package com.github.ncdhz.cat.tool.core;

import com.github.ncdhz.cat.tool.annotation.ScanPath;
import com.github.ncdhz.cat.tool.util.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author majunlong
 * 路径解析器
 * 用于解析出所用指定的类路径并把路径放入FilePath类中
 */
public class PathLoadApplication implements LoadApplication {

    private static final String FILE_SPLIT = "/";
    private static final String CLASS_SPLIT = "\\.";

    private static Logger logger = LoggerFactory.getLogger(PathLoadApplication.class);

    @Override
    public  void run(Class<?> clazz) throws Exception {
        logger.debug("{} path parsing begins",clazz);
        String path = clazz.getResource("").getPath();
        String packagePath = clazz.getPackage().getName();
        loadCatScanPath(clazz);
        loadPath(path,packagePath);
        logger.debug("{} Path parsing completed",clazz);
    }

    /**
     * 加载路径
     */
    private void loadPath(String path,String packagePath){
        FilePath.loadPath(path,packagePath);
    }
    /**
     * ScanPath 注解解析 返回一个 String 数组
     */
    private void loadCatScanPath(Class<?> clazz){
        logger.debug("{} analysis begins",ScanPath.class);
        ScanPath catScanPath = clazz.getAnnotation(ScanPath.class);
        if (catScanPath==null){
            logger.debug("{} analysis end",ScanPath.class);
            return;
        }
        String[] paths = catScanPath.paths();
        String rootPath = clazz.getResource("/").getPath();
        for (String path : paths) {
            if ("".equals(path)){
                continue;
            }
            path = path.replaceFirst(FILE_SPLIT, "");
            String classPath = path.replaceAll(FILE_SPLIT,CLASS_SPLIT);
            path = path.replaceAll(CLASS_SPLIT,FILE_SPLIT);
            loadPath(rootPath+path,classPath);
        }
        logger.debug("{} analysis end",ScanPath.class);
    }



}
