package com.peiel.notes.service;

import com.peiel.notes.model.EsArticle;

import java.util.List;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/29
 */
public interface EsService {

    List<EsArticle> searchArticleList(String kw);


}
