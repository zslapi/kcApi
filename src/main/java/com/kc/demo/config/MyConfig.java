package com.kc.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "myconfig")
@Component
public class MyConfig {
    @Value("/upload/images/comQuestion")
    private String imagesArticlePath;

    @Value("/upload/images/comQuestion")
    private String imagesComQuestionPath;

    @Value("/upload/images/comAnswer")
    private String imagesComAnswerPath;

    @Value("${myconfig.maximumPoolSize}")
    private String maximumPoolSize;

    //
    public String getImagesArticlePath() {
        return imagesArticlePath;
    }

    public void setImagesArticlePath(String imagesArticlePath) {
        this.imagesArticlePath = imagesArticlePath;
    }

    public String getImagesComQuestionPath() { return imagesComQuestionPath; }

    public void setImagesComQuestionPath(String imagesComQuestionPath) { this.imagesComQuestionPath = imagesComQuestionPath ;}

    public String getImagesComAnswerPath() { return imagesComAnswerPath; }

    public void setImagesComAnswerPath(String imagesComAnswerPath) { this.imagesComAnswerPath = imagesComAnswerPath ;}

    public String getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(String maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
}
