package com.liu.myblogapi.contrller;

import com.liu.myblogapi.Service.ArticleService;
import com.liu.myblogapi.vo.ArticleVo;
import com.liu.myblogapi.vo.params.ArticleParam;
import com.liu.myblogapi.vo.params.PageParams;
import com.liu.myblogapi.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {


    @Autowired
    private ArticleService articleService;
    //文章列表
    @PostMapping()
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }
    //热门文章
    @PostMapping("hot")
    public Result hotArticle(){
        int limit=5;
        return articleService.hotArticle(limit);
    }
    //最新文章
    @PostMapping("new")
    public Result newArticle(){
        int limit=5;
        return articleService.newArticle(limit);
    }
    //文章归档
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    //查看文章详情
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }
    //提交文章
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
