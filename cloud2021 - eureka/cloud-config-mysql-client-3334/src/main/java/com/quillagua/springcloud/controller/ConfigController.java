package com.quillagua.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laiyy
 * @date 2019/3/4 14:07
 * @description
 */
@RefreshScope
@RestController
public class ConfigController {

    @Value("${com.laiyy.gitee.config}")
    private String configInfo;

    @GetMapping(value = "/get-config-info")
    public String getConfigInfo(){
        return configInfo;
    }

}
