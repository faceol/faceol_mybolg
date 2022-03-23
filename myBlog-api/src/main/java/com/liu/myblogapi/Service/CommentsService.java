package com.liu.myblogapi.Service;

import com.liu.myblogapi.vo.Result;
import com.liu.myblogapi.vo.params.CommentParam;

public interface CommentsService {


    Result commentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}