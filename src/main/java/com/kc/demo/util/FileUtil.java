package com.kc.demo.util;

import org.slf4j.Logger;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;


public class FileUtil {

    /**
     * 获取项目运行根路径
     *
     * @return
     */
    public static String getBaseABSPath() throws FileNotFoundException {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        // 此种方式对于eclipse开发有效, 对jar运行方式无效
        if (!(path.contains(".jar") || path.contains(".JAR"))) {
            return path;
        }

        // 递归取到包含jar字样的那层目录
        // 去掉file:字符
        path = path.substring("file:/".length(), path.length());
        File jarFolder = getJarFolder(new File(path));
        ;
        if (!jarFolder.exists()) {
            //logger.error("Can not get jar folder: " + jarFolder.getPath());
            return null;
        }

        return jarFolder.getPath();
    }

    /**
     * 递归程序运行目录, 直到取到jar所在的目录为止
     *
     * @param file
     * @return
     */
    private static File getJarFolder(File file) {
        String fileName = file.getName();
        if (fileName.contains(".jar") || fileName.contains(".JAR")) {
            return file.getParentFile();
        } else {
            return getJarFolder(file.getParentFile());
        }
    }



}
