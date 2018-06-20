package com.kc.demo.dao;
import com.kc.demo.model.PraiseTread;

import java.util.HashMap;

public interface PraiseTreadMapper {
    /*根据文章id和用户id查找是否已点赞，点踩*/

    PraiseTread selectByArticleId(HashMap paramMap);

    /*根据评论id和用户id查找是否已点赞，点踩*/
    PraiseTread selectByCommentId(HashMap paramMap);

    int insert(PraiseTread praiseTread);

    int updatePraiseTread(PraiseTread praiseTread);

}
