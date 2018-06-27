package com.kc.demo.service.impl;

import com.kc.demo.dao.DictionaryMapper;
import com.kc.demo.model.Dictionary;
import com.kc.demo.service.DictionaryService;
import com.kc.demo.vo.DictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
   @Resource
   private DictionaryMapper dictionaryMapper;

    @Override
    public List<Dictionary> initDictionary(HttpServletRequest request){
         return dictionaryMapper.selectAllDictionnary();
    }

    @Override
    public List<Dictionary> selectByLikeName(Dictionary dictionary){
        return dictionaryMapper.selectByLikeName(dictionary);
    }
}
