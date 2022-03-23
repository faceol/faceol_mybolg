package com.liu.myblogapi.contrller;

import com.liu.myblogapi.Service.TagsService;
import com.liu.myblogapi.dao.mapper.TagMapper;
import com.liu.myblogapi.pojo.Tag;
import com.liu.myblogapi.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    private TagsService tagsService;
    @GetMapping("hot")
    public Result hot(int id){
        int limit=6;
        return tagsService.hots(limit);
    }
    @GetMapping
    public Result findAll(){
        return tagsService.findAll();
    }
    //根据tagid查询tag内容
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagsService.findDetailById(id);
    }
}
