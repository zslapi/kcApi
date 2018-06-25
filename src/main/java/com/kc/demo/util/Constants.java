package com.kc.demo.util;

import java.util.UUID;

public class Constants {

    /**
     * 自动生成32位的UUid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String serverUrl() {return "https://api.paimeigd.com/";}

    public static String articleImgFilePath() {return "/upload/images/article/";}
}
