package com.peiel.notes.dao;

import com.peiel.notes.model.TagWrapper;

import java.util.List;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/10
 */
public interface TagDao {

    List<TagWrapper> getTagList();

}
