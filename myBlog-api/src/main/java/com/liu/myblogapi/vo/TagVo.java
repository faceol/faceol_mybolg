package com.liu.myblogapi.vo;

public class TagVo {

    private Long id;

    private String tagName;

    public TagVo(Long id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public TagVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}