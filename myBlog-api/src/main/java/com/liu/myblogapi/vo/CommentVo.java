package com.liu.myblogapi.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

public class CommentVo  {

    //防止前端 精度损失 把id转为string
// 分布式id 比较长，传到前端 会有精度损失，必须转为string类型 进行传输，就不会有问题了
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;

    public CommentVo() {
    }

    public CommentVo(Long id, UserVo author, String content, List<CommentVo> childrens, String createDate, Integer level, UserVo toUser) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.childrens = childrens;
        this.createDate = createDate;
        this.level = level;
        this.toUser = toUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserVo getAuthor() {
        return author;
    }

    public void setAuthor(UserVo author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentVo> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<CommentVo> childrens) {
        this.childrens = childrens;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public UserVo getToUser() {
        return toUser;
    }

    public void setToUser(UserVo toUser) {
        this.toUser = toUser;
    }
}
