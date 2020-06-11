package com.peiel.notes.controller;

import com.alibaba.fastjson.JSON;
import com.peiel.notes.automation.mapper.ArticleTagMapper;
import com.peiel.notes.automation.mapper.TagMapper;
import com.peiel.notes.automation.model.Tag;
import com.peiel.notes.dao.TagDao;
import com.peiel.notes.model.TagWrapper;
import com.peiel.notes.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/10
 */
@RestController
@RequestMapping("/tag/")
public class TagController {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagDao tagDao;

    @GetMapping("getTagList")
    public JSON getTagList(Integer idx) {
        List<TagWrapper> list = tagDao.getTagList(idx);
        list = list.stream().sorted(Comparator.comparing(Tag::getName)).collect(Collectors.toList());
        ModelMap map = new ModelMap();
        map.put("list", list);
        return Util.jsonSuccess(map);
    }

}
