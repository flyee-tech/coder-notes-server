package com.peiel.notes.automation.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article_tag")
public class ArticleTag {
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;
    private java.lang.Integer articleId;
    private java.lang.Integer tagId;
    private java.lang.Integer status;
    private java.util.Date updatedTime;
    private java.util.Date createdTime;
}
