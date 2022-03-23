package com.liu.myblogapi.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.myblogapi.dao.mapper.ArticleBodyMapper;
import com.liu.myblogapi.dao.mapper.ArticleMapper;
import com.liu.myblogapi.dao.mapper.ArticleTagMapper;
import com.liu.myblogapi.dao.mapper.CategoryMapper;
import com.liu.myblogapi.dos.Archives;
import com.liu.myblogapi.pojo.Article;
import com.liu.myblogapi.pojo.ArticleBody;
import com.liu.myblogapi.pojo.ArticleTag;
import com.liu.myblogapi.pojo.SysUser;
import com.liu.myblogapi.utils.UserThreadLocal;
import com.liu.myblogapi.vo.*;
import com.liu.myblogapi.vo.params.ArticleParam;
import com.liu.myblogapi.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        //复制对应属性
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag){
            Long articleId = article.getId();
            List<TagVo> tagsByArticleId = tagsService.findTagsByArticleId(articleId);
            articleVo.setTags(tagsByArticleId);
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            SysUser userById = sysUserService.findUserById(authorId);
            articleVo.setAuthor(userById.getNickname());
        }
        if (isBody){
            //通过bodyid做关联查询
            ArticleBodyVo articleBody = findArticleBody(article.getBodyId());
            articleVo.setBody(articleBody);
        }
        if (isCategory){
            //通过CategoryId做关联查询
            CategoryVo categoryVo = categoryService.findCategoryById(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }

    @Autowired
    private CategoryMapper categoryMapper;
    private CategoryVo findCategory(Long categoryId) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(categoryVo,categoryMapper.selectById(categoryId));
        return categoryVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBody(Long id) {
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        BeanUtils.copyProperties(articleBodyVo,articleBodyMapper.selectById(id));
        return articleBodyVo;
    }

    //需要文章内容和类型的copy
    private List<ArticleVo> copylist(List<Article> records,boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record:records
        ) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }
    //重载，需要传入是否需要文章内容和类型的copy
    private List<ArticleVo> copylist(List<Article> records,boolean isTag, boolean isAuthor,boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record:records
        ) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }


    @Override
    //分页查询文章列表
    public Result listArticle(PageParams pageParams) {
        Page<Article> page=new Page<>(pageParams.getPage(),pageParams.getPagesize());
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //根据文章分类id查询，如果为空则全查询
        if (pageParams.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        //根据标签分类id查询，如果为空则全查询
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0){
                queryWrapper.in(Article::getId,articleIdList);
            }
        }
        //通过置顶排序,通过时间排序
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleVo> articleVoList=copylist(records,true,true);
        return Result.success(articleVoList);
    }

    //查看最热门文章limit条
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copylist(articles,false,false));
    }

    //查看最新文章limit条
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copylist(articles,false,false));
    }

    //通过时间统计文章
    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }



    //查看相应文章，并更新阅读数
    @Override
    public ArticleVo findArticleById(Long id) {
        /*
        * 1.根据id查询文章信息
        * 2.通过查询到的文章信息中的bodyid和categoryid做关联查询
        * */
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo=copy(article,true,true,true,true);

        //线程池操作，如果更新操作出错，不能影响查看文章的操作，把更新操作放入线程池，不会影响到查看的主线程
        threadService.updateViewCount(articleMapper,article);
        return articleVo;
    }

    //提交文章
    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();

        //插入article表
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);

        //插入tags表
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        //插入articleBody表
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);


        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }


}
