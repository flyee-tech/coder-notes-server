package com.peiel.notes.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.peiel.notes.automation.mapper.ArticleMapper;
import com.peiel.notes.automation.mapper.ArticleTagMapper;
import com.peiel.notes.automation.mapper.TagMapper;
import com.peiel.notes.automation.model.Article;
import com.peiel.notes.automation.model.ArticleTag;
import com.peiel.notes.automation.model.Tag;
import com.peiel.notes.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/8
 */
@Slf4j
@RestController
@RequestMapping("/article/")
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagMapper tagMapper;

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

    @GetMapping("getPublicList")
    public JSON getPublicList(String pn) {
        int pageSize = 10;
        int currentPage = pn != null && pn.trim().length() > 0 ? Integer.parseInt(pn) : 1;
        int pageNum = pageSize * (currentPage - 1);
        List<Article> list = articleMapper.selectList(Wrappers.lambdaQuery(new Article())
                .eq(Article::getStatus, 1)
                .eq(Article::getIsPublic, 1)
                .orderByDesc(Article::getId)
                .last("LIMIT " + pageNum + "," + pageSize));
        Integer count = articleMapper.selectCount(Wrappers.lambdaQuery(new Article())
                .eq(Article::getStatus, 1)
                .eq(Article::getIsPublic, 1));
        ModelMap map = new ModelMap();
        map.put("list", list);
        map.put("count", count);
        return Util.jsonSuccess(map);
    }

    @GetMapping("getListByTag")
    public JSON getListByTag(String pn, String tid, Integer idx) {
        int pageSize = 10;
        int currentPage = pn != null && pn.trim().length() > 0 ? Integer.parseInt(pn) : 1;
        int pageNum = pageSize * (currentPage - 1);


        List<ArticleTag> ats = articleTagMapper.selectList(Wrappers.lambdaQuery(new ArticleTag()).eq(ArticleTag::getStatus, 1).eq(ArticleTag::getTagId, tid));
        List<Integer> articleIds = ats.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());

        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery(new Article())
                .in(Article::getId, articleIds)
                .eq(Article::getStatus, 1);
        if (idx != null && idx == 1) {
            wrapper = wrapper.eq(Article::getIsPublic, 1);
        }
        List<Article> list = articleMapper.selectList(wrapper.orderByDesc(Article::getId).last("LIMIT " + pageNum + "," + pageSize));

        Integer count = articleMapper.selectCount(wrapper);
        ModelMap map = new ModelMap();
        map.put("list", list);
        map.put("count", count);
        return Util.jsonSuccess(map);
    }


    @GetMapping("getDetail")
    public JSON getDetail(Integer id) {
        Article article = articleMapper.selectById(id);
        List<Integer> ids = articleTagMapper.selectList(
                Wrappers.lambdaQuery(new ArticleTag())
                        .eq(ArticleTag::getArticleId, id)
                        .eq(ArticleTag::getStatus, 1))
                .stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        ModelMap map = new ModelMap();
        if (ids.size() != 0) {
            List<Tag> tags = tagMapper.selectList(Wrappers.lambdaQuery(new Tag()).in(Tag::getId, ids));
            map.put("tags", tags);
        }
        map.put("article", article);
        return Util.jsonSuccess(map);
    }

    @GetMapping("del")
    public JSON del(Integer id) {
        articleMapper.updateById(Article.builder().id(id).status(0).build());
        return Util.jsonSuccess();
    }

    @PostMapping("save")
    public JSON save(Article article, String tags) {
        article.setContent(URLDecoder.decode(article.getContent()));
        if (article.getId() == null) {
            articleMapper.insert(article);
        } else {
            articleMapper.updateById(article);
        }

        if (tags != null) {
            articleTagMapper.update(ArticleTag.builder().status(0).build(), Wrappers.lambdaQuery(new ArticleTag())
                    .eq(ArticleTag::getArticleId, article.getId()));
            String[] ts = tags.split(" ");
            for (String tag : ts) {
                tag = tag.replace("#", "").trim();
                // 查看标签库是否存在标签
                Tag t = tagMapper.selectOne(Wrappers.lambdaQuery(new Tag()).eq(Tag::getName, tag));
                if (t == null) {
                    t = Tag.builder().name(tag).build();
                    tagMapper.insert(t);
                }
                // 建立文章和标签的关联关系
                articleTagMapper.insert(ArticleTag.builder().articleId(article.getId()).tagId(t.getId()).build());
            }
        }

        ModelMap map = new ModelMap();
        map.put("id", article.getId());
        return Util.jsonSuccess(map);
    }

}
