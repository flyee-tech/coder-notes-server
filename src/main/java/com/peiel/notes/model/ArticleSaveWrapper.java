package com.peiel.notes.model;

import com.peiel.notes.automation.model.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleSaveWrapper extends Article {
    private String tags;
}
