package com.kc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.demo.config.MyConfig;
import com.kc.demo.dao.ComAnswerMapper;
import com.kc.demo.dao.CommunityImagesMapper;
import com.kc.demo.jobs.SaveImagesTask;
import com.kc.demo.model.Article;
import com.kc.demo.model.ComAnswer;
import com.kc.demo.model.CommunityImages;
import com.kc.demo.service.ComAnswerService;
import com.kc.demo.util.StringUtil;
import com.kc.demo.util.ThreadPoolUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class ComAnswerImpl implements ComAnswerService {

    @Resource
    private ComAnswerMapper comAnswerMapper;

    @Resource
    private MyConfig myConfig;

    @Resource
    private CommunityImagesMapper communityImagesMapper;

    /**
     * 发布答案
     * @param record
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int add(ComAnswer record) {
        comAnswerMapper.insert(record);
        int id = record.getId();
        return id;
    }

    /**
     * 对答案点赞
     * @param userId
     * @param comAnswerId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int praiseComAnswer(Integer userId,Integer comAnswerId) {

        return 0;
    }

    /**
     * 对答案点踩
     * @param userId
     * @param comAnswerId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int treadComAnswer(Integer userId,Integer comAnswerId){
        return 0;
    }

    /**
     * 添加答案的图片
     * @param imgFile
     * @param comAnswerId
     * @return
     * @throws FileNotFoundException
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public int addComAnswerImages(MultipartFile imgFile, Integer comAnswerId) throws FileNotFoundException {
        //先保存文件
        String fileName = null;
        String path = null;
        Callable<Object> task = new SaveImagesTask(imgFile,myConfig.getImagesArticlePath());
        Future<Object> taskResult = ThreadPoolUtil.submit(task);
        Map<String,Object> resultMap = SaveImagesTask.getSaveImgPath(taskResult);
        fileName = (String) resultMap.get("fileName");
        path = (String)resultMap.get("path");
        int comQuestionImgId = 0;
        if(!StringUtil.isEmpty(fileName)&&!StringUtil.isEmpty(path)){
            CommunityImages communityImages = new CommunityImages();
            communityImages.setOriginalname(imgFile.getOriginalFilename());
            communityImages.setFilename(fileName);
            communityImages.setComanswerid(comAnswerId);
            communityImages.setImgtype(imgFile.getContentType());
            communityImages.setFilepath(path);
            communityImagesMapper.insert(communityImages);
            comQuestionImgId = communityImages.getId();
        }
        return comQuestionImgId;
    }

    /**
     * 根据问题ID获取答案列表
     * @param comQuestionId
     * @param pageNum
     * @param pageSize
     * @return
     */

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public Map<String,Object> getComAnswerListByQueId(Integer comQuestionId,Integer pageNum,Integer pageSize){
        Map<String,Object> resultMap = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ComAnswer> comAnswerList = comAnswerMapper.selectByComQuestionId(comQuestionId);
        PageInfo<ComAnswer> pageInfo = new PageInfo<>(comAnswerList);
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("list",pageInfo.getList());
        return resultMap;
    }

}
