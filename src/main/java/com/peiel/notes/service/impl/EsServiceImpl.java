package com.peiel.notes.service.impl;

import com.peiel.notes.model.EsArticle;
import com.peiel.notes.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/29
 */
@Slf4j
@Service
public class EsServiceImpl implements EsService {
    @Autowired
    private ElasticsearchOperations operations;

    @Override
    public List<EsArticle> searchArticleList(String kw) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        NativeSearchQuery query = builder.withQuery(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name", kw)).boost(2)
                .should(QueryBuilders.matchQuery("name.ik", kw)).boost(1)
                .should(QueryBuilders.matchQuery("name.pinyin", kw).boost(2))
                .should(QueryBuilders.matchQuery("name.ik_pinyin", kw).boost(1))
                .should(QueryBuilders.matchQuery("content", kw)).boost(2)
                .should(QueryBuilders.matchQuery("content.ik", kw)).boost(1)
                .should(QueryBuilders.matchQuery("content.pinyin", kw).boost(2))
                .should(QueryBuilders.matchQuery("content.ik_pinyin", kw).boost(1)))
                .withHighlightFields(
                        new HighlightBuilder.Field("name"),
                        new HighlightBuilder.Field("name.ik"),
                        new HighlightBuilder.Field("name.pinyin"),
                        new HighlightBuilder.Field("name.ik_pinyin"),
                        new HighlightBuilder.Field("content"),
                        new HighlightBuilder.Field("content.ik"),
                        new HighlightBuilder.Field("content.pinyin"),
                        new HighlightBuilder.Field("content.ik_pinyin"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                .build();
        log.debug("DSL:{}", query.getQuery().toString());

        SearchHits<EsArticle> searchHits = operations.search(query, EsArticle.class, IndexCoordinates.of("article"));

        List<EsArticle> result = new ArrayList<>();
        for (SearchHit<EsArticle> searchHit : searchHits) {
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            EsArticle esArticle = searchHit.getContent();
            for (String key : highlightFields.keySet()) {
                if (key.startsWith("name")) {
                    esArticle.setName(highlightFields.get(key).get(0));
                }
            }
            for (String key : highlightFields.keySet()) {
                if (key.startsWith("content")) {
                    esArticle.setContent(highlightFields.get(key).get(0));
                }
            }
            result.add(esArticle);
        }
        return result;
    }
}
