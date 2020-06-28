package com.peiel.notes;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.peiel.notes.automation.mapper.ArticleMapper;
import com.peiel.notes.automation.model.Article;
import com.peiel.notes.es.ElasticsearchArticleRepository;
import com.peiel.notes.model.EsArticle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void createInxAndMapping() {
        IndexOperations idxOpt = esTemplate.indexOps(EsArticle.class);
        boolean d = idxOpt.delete();
        System.out.println("isDelete : " + d);
        boolean b = idxOpt.create(Document.parse("{\"analysis\":{\"analyzer\":{\"pinyin_analyzer\":{\"tokenizer\":\"my_pinyin\"}},\"tokenizer\":{\"my_pinyin\":{\"type\":\"pinyin\",\"keep_separate_first_letter\":false,\"keep_full_pinyin\":true,\"keep_original\":true,\"limit_first_letter_length\":16,\"lowercase\":true,\"remove_duplicated_term\":true}}}}"));
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
        String q = "haxi";
        List<EsArticle> list = elasticsearchArticleRepository.findByNameOrContent(q, q).stream().limit(5).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(list));
    }

}
