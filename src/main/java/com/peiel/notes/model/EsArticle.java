package com.peiel.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

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
    @MultiField(
            mainField = @Field(type = FieldType.Keyword),
            otherFields = {
                    @InnerField(type = FieldType.Text, suffix = "ik", analyzer = "ik_max_word", searchAnalyzer = "ik_max_word"),
                    @InnerField(type = FieldType.Text, suffix = "ik_pinyin", analyzer = "ik_pinyin_analyzer", searchAnalyzer = "ik_pinyin_analyzer"),
                    @InnerField(type = FieldType.Text, suffix = "pinyin", analyzer = "pinyin_analyzer", searchAnalyzer = "pinyin_analyzer")
            }
    )
    private java.lang.String name;
    @MultiField(
            mainField = @Field(type = FieldType.Keyword),
            otherFields = {
                    @InnerField(type = FieldType.Text, suffix = "ik", analyzer = "ik_max_word", searchAnalyzer = "ik_max_word"),
                    @InnerField(type = FieldType.Text, suffix = "ik_pinyin", analyzer = "ik_pinyin_analyzer", searchAnalyzer = "ik_pinyin_analyzer"),
                    @InnerField(type = FieldType.Text, suffix = "pinyin", analyzer = "pinyin_analyzer", searchAnalyzer = "pinyin_analyzer")
            }
    )
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
