package com.quillagua.springcloud.controller;

import com.quillagua.springcloud.service.IHelloService;
import com.quillagua.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @auther zzyy
 * @create 2020-02-20 11:57
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystirxController
{
    @Resource
    private PaymentHystrixService paymentHystrixService;

    @Resource
    private RestTemplate restTemplate;

    private final IHelloService helloService;

    @Autowired
    public OrderHystirxController(RestTemplate restTemplate, IHelloService helloService) {
        this.restTemplate = restTemplate;
        this.helloService = helloService;
    }

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id)
    {
        String result = paymentHystrixService.paymentInfo_OK(id);
        return result;
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="8000")
    })
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id)
    {
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }
    public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id)
    {
        return "我是消费者80,对方支付系统繁忙请10秒钟后再试或者自己运行出错请检查自己,o(╥﹏╥)o";
    }

    // 下面是全局fallback方法
    public String payment_Global_FallbackMethod()
    {
        return "Global异常处理信息，请稍后再试，/(ㄒoㄒ)/~~";
    }

//
//
//    /**
//     * 缓存测试
//     */
//    @GetMapping(value = "/get-user/{id}")
//    public String getUser(@PathVariable int id) {
//        helloService.hello(id);
//        helloService.hello(id);
//        helloService.hello(id);
//        helloService.hello(id);
//        return "getUser success!";
//    }
//
//    /**
//     * 缓存更新
//     */
//    @GetMapping(value = "/get-user-id-update/{id}")
//    public String getUserIdUpdate(@PathVariable int id){
//        helloService.hello(id);
//        helloService.hello(id);
//        helloService.hello(5);
//        helloService.hello(5);
//        return "getUserIdUpdate success!";
//    }
//
//    /**
//     * 缓存、清除缓存
//     */
//    @GetMapping(value = "/get-and-update/{id}")
//    public String getAndUpdateUser(@PathVariable int id){
//        // 缓存数据
//        helloService.getUserToCommandKey(id);
//        helloService.getUserToCommandKey(id);
//
//        // 缓存清除
//        helloService.updateUser(id);
//
//        // 再次缓存
//        helloService.getUserToCommandKey(id);
//        helloService.getUserToCommandKey(id);
//
//        return "getAndUpdateUser success!";
//    }
}
