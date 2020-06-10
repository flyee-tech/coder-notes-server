package com.peiel.notes.model;

import com.peiel.notes.automation.model.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagWrapper extends Tag {

    private Integer count;
    private String style;

    public String getStyle() {
        String defaultFont = "font-size: 20px;";
        if (count != null && count > 0) {
            return "font-size: " + (20 + count * 2) + "px;";
        }

        return defaultFont;
    }

}
