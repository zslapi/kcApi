package com.kc.demo.jobs;

import com.kc.demo.util.Constants;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class SaveImagesTask implements Callable<Object> {
    /**
     *请求文件
     */
    private MultipartFile multipartFile;

    /**
     * 文件路径
     */
    private String path;

    public SaveImagesTask(MultipartFile multipartFile,String path){
        this.path = path;
        this.multipartFile = multipartFile;
    }

    @Override
    public Object call() throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = null;
        BufferedOutputStream bos = null;
        try {
            FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
            String originalFileName = multipartFile.getOriginalFilename();
            String suffix = getImgSuffix(originalFileName);
            fileName = Constants.getUUID() + "."+suffix;
            bos = new BufferedOutputStream(new FileOutputStream(path + File.separator + fileName));
            byte[] bs = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bs)) != -1) {
                bos.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bos!=null){
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("fileName",fileName);
        resultMap.put("path",path);
        return resultMap;
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
