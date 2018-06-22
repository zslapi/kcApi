package com.kc.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "myconfig")
@Component
public class MyConfig {
    @Value("${myconfig.imagesPath}")
    private String imagesArticlePath;

    @Value("/kcApi/upload/images/comQuestion")
    private String imagesComQuestionPath;

    @Value("${myconfig.maximumPoolSize}")
    private String maximumPoolSize;

    //
    public String getImagesArticlePath() {
        return imagesArticlePath;
    }

    public void setImagesArticlePath(String imagesArticlePath) {
        this.imagesArticlePath = imagesArticlePath;
    }

    public String getImagesComQuestion() { return imagesComQuestionPath; }

    public void setImagesComQuestionPath(String imagesComQuestionPath) { this.imagesComQuestionPath = imagesComQuestionPath ;}

    public String getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(String maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
}
