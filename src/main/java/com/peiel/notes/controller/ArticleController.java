package com.peiel.notes.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.peiel.notes.automation.mapper.ArticleMapper;
import com.peiel.notes.automation.model.Article;
import com.peiel.notes.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/8
 */
@RestController
@RequestMapping("/article/")
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("getList")
    public JSON getList() {
        List<Article> list = articleMapper.selectList(Wrappers.lambdaQuery(new Article())
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getId)
                .last("limit 20"));
        ModelMap map = new ModelMap();
        map.put("list", list);
        return Util.jsonSuccess(map);
    }

}
