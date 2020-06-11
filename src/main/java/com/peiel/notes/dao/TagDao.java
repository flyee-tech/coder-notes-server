package com.peiel.notes.dao;

import com.peiel.notes.model.TagWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/10
 */
public interface TagDao {

    List<TagWrapper> getTagList(@Param("idx") Integer idx);

}
