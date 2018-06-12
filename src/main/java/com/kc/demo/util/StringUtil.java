package com.kc.demo.util;

/**
 * String相关操作工具类
 */
public class StringUtil {
    public static final boolean isEmpty(String s){
        return (s==null || s.trim().length()==0);
    }
}
