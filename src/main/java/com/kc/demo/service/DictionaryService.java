package com.kc.demo.service;

import javax.servlet.http.HttpServletRequest;

import com.kc.demo.model.Dictionary;
import com.kc.demo.vo.DictionaryVo;

import java.util.List;

public interface DictionaryService {
   public List<Dictionary> initDictionary(HttpServletRequest request);

}
