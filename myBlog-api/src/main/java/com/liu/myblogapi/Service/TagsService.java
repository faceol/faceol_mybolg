package com.liu.myblogapi.Service;

import com.liu.myblogapi.pojo.Tag;
import com.liu.myblogapi.vo.Result;
import com.liu.myblogapi.vo.TagVo;

import java.util.List;

public interface TagsService {

    List<TagVo> findTagsByArticleId(Long id);

    Result hots(int limit);


    Result findAll();

    Result findDetailById(Long id);
}
