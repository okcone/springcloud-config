package com.quillagua.springcloud.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @auther zzyy
 * @create 2020-02-18 10:43
 */
@RestController
@Slf4j
public class PaymentController
{

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/provider/add")
    public String add(Integer a, Integer b, HttpServletRequest request) {
        return "From Port: " + request.getServerPort() + ", Result: " + (a + b);
    }

}
