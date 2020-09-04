package com.quillagua.springcloud;

import com.quillagua.springcloud.dao.ZuulPropertiesDao;
import com.quillagua.springcloud.entity.ZuulRouteEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laiyy
 * @date 2019/2/20 17:19
 * @description
 */
public class DynamicZuulRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private ZuulPropertiesDao zuulPropertiesDao;

    public DynamicZuulRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.zuulProperties = properties;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        Map<String, ZuulProperties.ZuulRoute> routeMap = new LinkedHashMap<>();
        routeMap.putAll(super.locateRoutes());
        routeMap.putAll(getProperties());
        Map<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        routeMap.forEach((path, zuulRoute) -> {
            path = path.startsWith("/") ? path : "/" + path;
            if (StringUtils.hasText(this.zuulProperties.getPrefix())) {
                path = this.zuulProperties.getPrefix() + path;
                path = path.startsWith("/") ? path : "/" + path;
            }
            values.put(path, zuulRoute);
        });
        return values;
    }

    private Map<String, ZuulProperties.ZuulRoute> getProperties() {
        Map<String, ZuulProperties.ZuulRoute> routeMap = new LinkedHashMap<>();
        List<ZuulRouteEntity> list = zuulPropertiesDao.findAllByParams();
        list.forEach(entity -> {
            if (org.apache.commons.lang.StringUtils.isBlank(entity.getPath())) {
                return;
            }
            ZuulProperties.ZuulRoute route = new ZuulProperties.ZuulRoute();
            BeanUtils.copyProperties(entity, route);
            route.setId(String.valueOf(entity.getId()));
            routeMap.put(route.getPath(), route);
        });
        return routeMap;
    }
}
