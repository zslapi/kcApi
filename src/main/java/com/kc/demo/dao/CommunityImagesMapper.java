package com.kc.demo.dao;

import com.kc.demo.model.CommunityImages;

import java.util.List;

public interface CommunityImagesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommunityImages record);

    int insertSelective(CommunityImages record);

    CommunityImages selectByPrimaryKey(Integer id);

    List<CommunityImages> selectCommunityImageListByQueId(Integer comQuestionId);

    List<CommunityImages> selectCommunityImageListByAnsId(Integer comAnswerId);

    int updateByPrimaryKeySelective(CommunityImages record);

    int updateByPrimaryKey(CommunityImages record);
}
