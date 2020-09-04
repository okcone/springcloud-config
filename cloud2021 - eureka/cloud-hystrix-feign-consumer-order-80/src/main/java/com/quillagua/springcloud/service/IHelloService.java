package com.quillagua.springcloud.service;

import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;

/**
 * @author laiyy
 * @date 2019/2/11 14:03
 * @description
 */
public interface IHelloService {

    String hello(int id);

    String getUserToCommandKey(@CacheKey int id);

    String updateUser(@CacheKey int id);

}
