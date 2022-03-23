package com.liu.myblogapi.vo.params;

public class PageParams {
    private int page=1;
    private int pagesize=10;
    private Long categoryId;
    private Long tagId;
    private String year;
    private String month;

    public PageParams(int page, int pagesize, Long categoryId, Long tagId, String year, String month) {
        this.page = page;
        this.pagesize = pagesize;
        this.categoryId = categoryId;
        this.tagId = tagId;
        this.year = year;
        this.month = month;
    }

    public PageParams() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
