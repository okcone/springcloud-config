package com.quillagua.springcloud.controller;


import com.quillagua.springcloud.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laiyy
 * @date 2019/2/11 14:31
 * @description
 */
@RestController
public class UserController {

    @GetMapping(value = "/get-user/{id}")
    public User getUser(@PathVariable int id) {
        switch (id) {
            case 1:
                return new User("zhangsan", "list", 22);
            case 2:
                return new User("laiyy", "123456", 24);
            default:
                return new User("hahaha", "error", 0);
        }
    }

}
