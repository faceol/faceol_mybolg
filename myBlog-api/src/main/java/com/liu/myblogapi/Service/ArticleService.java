package com.liu.myblogapi.Service;

import com.liu.myblogapi.vo.ArticleVo;
import com.liu.myblogapi.vo.params.ArticleParam;
import com.liu.myblogapi.vo.params.PageParams;
import com.liu.myblogapi.vo.Result;

public interface ArticleService {


    //分页查询文章列表
    Result listArticle(PageParams pageParams);
    //最热门文章
    Result hotArticle(int limit);

    Result newArticle(int limit);

    Result listArchives();

    ArticleVo findArticleById(Long id);

    Result publish(ArticleParam articleParam);
}
