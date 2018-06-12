package com.kc.demo.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {
    /**
     * 保存文件，直接以multipartFile形式
     *
     * @param multipartFile
     * @return 返回文件名
     * @throws IOException
     */
    public static String saveImg(MultipartFile multipartFile, String path){

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

        return fileName;
    }

    public static Map<String,String> downloadFileFromRemotePath(String remotePath) throws FileNotFoundException {
        Map<String,String> resultMap = new HashMap<>();
        String absPath = FileUtil.getBaseABSPath();
        String fileName = Constants.getUUID();
        String suffix = getImgSuffix(remotePath);
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection conn = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(remotePath);
            conn = (HttpURLConnection) url.openConnection();
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36");
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            System.out.println("获取图片链接成功，链接为："+remotePath);
            bis = new BufferedInputStream(inputStream);
            String localPath = absPath+File.separator+fileName+"."+suffix;
            fos = new FileOutputStream(localPath);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            resultMap.put("localPath",localPath);
            resultMap.put("fileName",fileName);
            resultMap.put("suffix",suffix);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                bis.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultMap;
    }

    public static Map saveToImgByInputStream (InputStream in,String imgPath){
        //将流转成临时存储文件，拿到属性存储在map里面然后删除临时文件。
        Map map =null;
        FileOutputStream fos=null;
        BufferedImage bufferedImage=null;
        File file,f;
        byte[] b=null;
        FileInputStream fis=null;
        map =new HashMap();

        try {
            // 将上面生成的图片格式字符串 imgStr，还原成图片显示
            String imgName = Constants.getUUID();
            file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
            fos=new FileOutputStream(file);
            b = new byte[1024];
            int nRead = 0;
            while ((nRead = in.read(b)) != -1) {
                fos.write(b, 0, nRead);
            }
            fos.flush();
            map.put("imgName",imgName);
            map.put("size", getPrintSize(file.length()));
            bufferedImage = ImageIO.read(file);
            map.put("width", bufferedImage.getWidth());
            map.put("height", bufferedImage.getHeight());

            //这里为什么要新得到一个流呢，因为上面流写入完了，发现传到存储服务器的文件是空的，个人猜测，流空了，没有深入研究过=.=
//            f=new File(imgPath+"/"+imgName);
//            fis=new FileInputStream(f);

            //这个方法是将文件传到存储服务器，暂时存储本地
//            saveFileToFtpServer(fis, path, imgName);
//            map.put("url",Constant.systemUserCenterFileServerUrl+path+"/"+imgName);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("errorMsg",e.getMessage());
        } finally {
            bufferedImage = null;
            file=null;
            try {
                if(fos!=null){
                    fos.close();
                }
                if(fis!=null){
                    fis.close();
                    fis=null;
                }
                f=null;
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return map;
        }

        //return map;
    }

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
     * 删除指定文件或者指定文件夹下的文件
     * @param folderPath	路径
     */
    public static void delFolder(String folderPath) {
        File myFilePath=null;
        try {
            //delAllFile(folderPath); // 删除完里面所有内容
            myFilePath = new File(folderPath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            myFilePath=null;
        }
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
