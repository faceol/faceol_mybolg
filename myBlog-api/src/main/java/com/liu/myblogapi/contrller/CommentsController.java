package com.liu.myblogapi.contrller;

import com.liu.myblogapi.Service.CommentsService;
import com.liu.myblogapi.vo.Result;
import com.liu.myblogapi.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {


    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId){

        System.out.println("sssccc");
        return commentsService.commentsByArticleId(articleId);
    }
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}