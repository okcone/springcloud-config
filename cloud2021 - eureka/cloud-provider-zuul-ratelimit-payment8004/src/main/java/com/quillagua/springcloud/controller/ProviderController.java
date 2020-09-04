package com.quillagua.springcloud.controller;


import org.springframework.web.bind.annotation.*;


/**
 * @author laiyy
 * @date 2019/2/20 14:49
 * @description
 */
@RestController
public class ProviderController {

    @GetMapping(value = "/get-result")
    public String result(){
        return "zuul rate limit result !";
    }

    @GetMapping(value = "/hello")
    public String hello(String apple){
        return "say "+ apple +" hello world !";
    }

}
