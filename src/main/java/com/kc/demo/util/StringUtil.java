package com.kc.demo.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * String相关操作工具类
 */
public class StringUtil {
    public static final boolean isEmpty(String s){
        return (s==null || s.trim().length()==0);
    }

    /**
     * 根据创建时间获取距离当前时间的时长
     * @param createTimeDate
     * @return
     */
    public static String getTimeAgoAsString(Date createTimeDate) {
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        String timeAgo = "";
        if(createTimeDate != null) {
            Timestamp articleCreateTime = new Timestamp(createTimeDate.getTime());
            long dayAgo = (nowTime.getTime()-articleCreateTime.getTime())/(1000*60*60*24);
            long hourAgo = (nowTime.getTime()-articleCreateTime.getTime())/(1000*60*60);
            long minuteAgo = (nowTime.getTime()-articleCreateTime.getTime())/(1000*60);
            if(dayAgo>=1){
                timeAgo = (int)dayAgo+"天前";
            } else if (hourAgo>0&&hourAgo<24) {
                timeAgo = (int)hourAgo+"小时前";
            } else if (minuteAgo>0) {
                timeAgo = (int)minuteAgo+"分钟前";
            }

        }
        return timeAgo;
    }
}
