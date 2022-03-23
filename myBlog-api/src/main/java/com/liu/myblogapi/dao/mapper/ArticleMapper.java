package com.liu.myblogapi.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.myblogapi.dos.Archives;
import com.liu.myblogapi.pojo.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();
}
