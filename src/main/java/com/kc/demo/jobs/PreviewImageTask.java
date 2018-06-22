package com.kc.demo.jobs;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class PreviewImageTask implements Callable<Object> {
    //图片存放路径
    private String imagesPath;

    //文件名
    private String fileName;

    //图片ResponseEntity
    private ResponseEntity<?> responseEntity;

    public PreviewImageTask (String fileName,String ImagesPath) {
        this.fileName = fileName;
        this.imagesPath = ImagesPath;
    }

    @Override
    public Object call() throws Exception {
        InputStream inputStream = new FileInputStream(new File(imagesPath+fileName));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        responseEntity = new ResponseEntity<>(inputStreamResource,headers, HttpStatus.OK);
        return responseEntity;
    }
}
