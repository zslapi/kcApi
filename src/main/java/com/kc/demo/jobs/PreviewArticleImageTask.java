package com.kc.demo.jobs;

import com.kc.demo.bean.MyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class PreviewArticleImageTask implements Callable<Object> {
    //文章图片存放路径
    private String articleImagesPath;

    //文件名
    private String fileName;

    //图片ResponseEntity
    private ResponseEntity<?> responseEntity;

    public PreviewArticleImageTask (String fileName,String articleImagesPath) {
        this.fileName = fileName;
        this.articleImagesPath = articleImagesPath;
    }

    @Override
    public Object call() throws Exception {
        InputStream inputStream = new FileInputStream(new File(articleImagesPath+fileName));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        responseEntity = new ResponseEntity<>(inputStreamResource,headers, HttpStatus.OK);
        return responseEntity;
    }
}
