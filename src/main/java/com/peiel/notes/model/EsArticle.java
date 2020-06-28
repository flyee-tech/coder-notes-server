package com.peiel.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "article")
public class EsArticle {
    @Id
    private java.lang.Integer id;
    @Field(type = FieldType.Text, analyzer = "pinyin_analyzer")
    private java.lang.String name;
    @Field(type = FieldType.Text, analyzer = "pinyin_analyzer")
    private java.lang.String content;
    @Field(type = FieldType.Integer)
    private java.lang.Integer type;
    @Field(type = FieldType.Integer)
    private java.lang.Integer isPublic;
    @Field(type = FieldType.Integer)
    private java.lang.Integer status;
    @Field(type = FieldType.Date)
    private java.util.Date updatedTime;
    @Field(type = FieldType.Date)
    private java.util.Date createdTime;
}
