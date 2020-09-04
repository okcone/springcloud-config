package com.quillagua.springcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.Controller;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author laiyy
 * @date 2019/2/11 14:15
 * @description
 */
@Configuration
public class CacheConfiguration {

    /**
     * 声明一个 cacheContextInterceptor 注入 Spring 容器
     */
    @Bean
    @ConditionalOnClass(Controller.class)
    public CacheContextInterceptor cacheContextInterceptor(){
        return new CacheContextInterceptor();
    }

    @Configuration
    @ConditionalOnClass(Controller.class)
    public class WebMvcConfig extends WebMvcConfigurationSupport{

        private final CacheContextInterceptor interceptor;

        @Autowired
        public WebMvcConfig(CacheContextInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        /**
         * 将 cacheContextInterceptor 添加到拦截器中
         */
        @Override
        protected void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(interceptor);
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            super.configureMessageConverters(converters);
            converters.add(responseBodyConverter());
        }
        @Bean
        public HttpMessageConverter<String> responseBodyConverter() {
            return new StringHttpMessageConverter(Charset.forName("UTF-8"));
        }

        @Override
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            configurer.favorPathExtension(false);
        }
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }



}
