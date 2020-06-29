package com.peiel.notes;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.peiel.notes.automation.mapper.ArticleMapper;
import com.peiel.notes.automation.model.Article;
import com.peiel.notes.es.ElasticsearchArticleRepository;
import com.peiel.notes.model.EsArticle;
import com.peiel.notes.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/28
 */
@Slf4j
@SpringBootTest
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchRestTemplate esTemplate;
    @Autowired
    private ElasticsearchArticleRepository elasticsearchArticleRepository;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private EsService esService;

    @Test
    public void createInxAndMapping() {
        IndexOperations idxOpt = esTemplate.indexOps(EsArticle.class);
        boolean d = idxOpt.delete();
        System.out.println("isDelete : " + d);
        boolean b = idxOpt.create(Document.parse("{\"number_of_replicas\":\"0\",\"number_of_shards\":\"1\",\"analysis\":{\"analyzer\":{\"ik_pinyin_analyzer\":{\"tokenizer\":\"my_ik_pinyin\",\"filter\":\"pinyin_first_letter_and_full_pinyin_filter\"},\"pinyin_analyzer\":{\"tokenizer\":\"my_pinyin\"}},\"tokenizer\":{\"my_ik_pinyin\":{\"type\":\"ik_max_word\"},\"my_pinyin\":{\"type\":\"pinyin\",\"keep_first_letter\":true,\"keep_separate_first_letter\":false,\"keep_full_pinyin\":false,\"keep_joined_full_pinyin\":true,\"keep_none_chinese\":true,\"none_chinese_pinyin_tokenize\":false,\"keep_none_chinese_in_joined_full_pinyin\":true,\"keep_original\":false,\"limit_first_letter_length\":16,\"lowercase\":true,\"trim_whitespace\":true,\"remove_duplicated_term\":true}},\"filter\":{\"pinyin_first_letter_and_full_pinyin_filter\":{\"type\":\"pinyin\",\"keep_first_letter\":true,\"keep_separate_first_letter\":false,\"keep_full_pinyin\":false,\"keep_joined_full_pinyin\":true,\"keep_none_chinese\":true,\"none_chinese_pinyin_tokenize\":false,\"keep_none_chinese_in_joined_full_pinyin\":true,\"keep_original\":false,\"limit_first_letter_length\":16,\"lowercase\":true,\"trim_whitespace\":true,\"remove_duplicated_term\":true}}}}"));
        System.out.println("idCreate : " + b);
        Document document = idxOpt.createMapping(EsArticle.class);
        System.out.println(JSON.toJSONString(document));
        boolean isPut = idxOpt.putMapping(document);
        System.out.println(isPut);
    }

    @Test
    public void test() {
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery(new Article()).eq(Article::getStatus, 1);
        List<Article> source = articleMapper.selectList(wrapper);

        List<EsArticle> dest = new ArrayList<>();

        for (Article article : source) {
            EsArticle esArticle = new EsArticle();
            esArticle.setId(article.getId());
            esArticle.setName(article.getName());
            esArticle.setContent(article.getContent());
            esArticle.setType(article.getType());
            esArticle.setIsPublic(article.getIsPublic());
            esArticle.setStatus(article.getStatus());
            esArticle.setUpdatedTime(article.getUpdatedTime());
            esArticle.setCreatedTime(article.getCreatedTime());
            dest.add(esArticle);
        }

        elasticsearchArticleRepository.saveAll(dest);

        Iterable<EsArticle> all = elasticsearchArticleRepository.findAll();
        for (EsArticle a : all) {
            System.out.println(a);
        }

    }

    @Test
    public void testQuery() {
        String q = "jg";
//        List<EsArticle> list = elasticsearchArticleRepository.findByNameOrContent(q, q).stream().limit(5).collect(Collectors.toList());
//        List<EsArticle> list = elasticsearchArticleRepository.findByName(q).stream().limit(5).collect(Collectors.toList());
//        for (EsArticle esArticle : list) {
//            System.out.println(esArticle.getName());
//        }

        esService.searchArticleList(q);

//        Iterable<EsArticle> it = elasticsearchArticleRepository.search(query);
//        Lists.newArrayList(it);
//        for (EsArticle esArticle : it) {
//            System.out.println(esArticle.getName());
//        }
    }

}
