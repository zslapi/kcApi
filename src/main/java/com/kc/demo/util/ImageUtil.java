package com.kc.demo.util;

import com.kc.demo.bean.MyConfig;
import com.kc.demo.jobs.PreviewArticleImageTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class ImageUtil {
    /**
     * 将文件大小b转为mb
     * @param size
     * @return
     */
    public static String getPrintSize(long size) {
    //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        //if (size < 1024) {
        //因为如果以MB为单位的话，要保留最后1位小数，
        //因此，把此数乘以100之后再取余
        size = size * 100;
        return String.valueOf((size / 100)) + "."+ String.valueOf((size % 100)) + "MB";
        /*} else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
            + String.valueOf((size % 100)) + "GB";
        } */
    }


    /**
     * 获取图片格式后缀
     * @param full
     * @return
     */
    private static String getImgSuffix(String full){
        if(StringUtils.isEmpty(full)){
            return "";
        }
        String[] strs = full.split("[.]");
        String suffix = strs[strs.length-1];
        return suffix;
    }

}
