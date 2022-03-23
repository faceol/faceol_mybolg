package com.liu.myblogapi.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.myblogapi.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    //根据文章id查询标签列表
    List<Tag> findTagsByArticleId(Long id);
    //查询最热标签前limit条的id
    List<Long> findHotsTagIds(int limit);
    //根据tagid查询tag
    List<Tag> findTagsByIds(List<Long> tagIds);
}
