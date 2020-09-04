package com.quillagua.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author laiyy
 * @date 2019/2/11 14:04
 * @description
 */
@Component
public class HelloServiceImpl implements IHelloService {

    private final RestTemplate restTemplate;

    @Autowired
    public HelloServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @CacheResult
    @HystrixCommand
    public String hello(int id) {
        String result = restTemplate.getForObject("http://cloud-hystrix-provider-payment/get-user/{1}", String.class, id);
        System.out.println("正在进行远程调用：hello " + result);
        return result;
    }

    @Override
    @CacheResult
    @HystrixCommand(commandKey = "getUser")
    public String getUserToCommandKey(int id) {
        String result = restTemplate.getForObject("http://cloud-hystrix-provider-payment/get-user/{1}", String.class, id);
        System.out.println("正在进行远程调用：getUserToCommandKey " + result);
        return result;
    }

    @Override
    @CacheRemove(commandKey = "getUser1")
    @HystrixCommand
    public String updateUser(int id) {
        System.out.println("正在进行远程调用：updateUser " + id);
        return "update success";
    }
}
