package com.quillagua.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author laiyy
 * @date 2019/2/21 10:12
 * @description
 */
@Configuration
public class DynamicZuulConfig {

    private final ZuulProperties zuulProperties;

    private final ServerProperties serverProperties;

    @Autowired
    public DynamicZuulConfig(ZuulProperties zuulProperties, ServerProperties serverProperties) {
        this.zuulProperties = zuulProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    public DynamicZuulRouteLocator dynamicZuulRouteLocator(){
        return new DynamicZuulRouteLocator(serverProperties.getServlet().getContextPath(), zuulProperties);
    }
}
