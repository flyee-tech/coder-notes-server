package com.peiel.notes.es;

import com.peiel.notes.model.EsArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/28
 */
public interface ElasticsearchArticleRepository extends ElasticsearchRepository<EsArticle, Long> {

    List<EsArticle> findByNameOrContent(String name, String content);
    List<EsArticle> findByName(String name);

}
