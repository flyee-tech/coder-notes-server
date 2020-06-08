package com.peiel.notes.controller;

import com.alibaba.fastjson.JSON;
import com.peiel.notes.automation.mapper.UserMapper;
import com.peiel.notes.automation.model.User;
import com.peiel.notes.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peiel
 * @version V1.0
 * @date 2020/6/8
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("getById")
    public JSON getById(Integer id) {
        User user = userMapper.selectById(1);
        ModelMap map = new ModelMap();
        map.put("user", user);
        return Util.jsonSuccess(map);
    }

}
