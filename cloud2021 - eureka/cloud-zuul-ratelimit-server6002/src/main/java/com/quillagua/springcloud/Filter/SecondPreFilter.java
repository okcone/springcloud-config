package com.quillagua.springcloud.Filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xwm on 2020/8/26.
 */
public class SecondPreFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecondPreFilter.class);
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        LOGGER.info(">>>>>>>>>>>>> SecondPreFilter ！ <<<<<<<<<<<<<<<<");
        // 获取上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 从上下文获取 request
        HttpServletRequest request = requestContext.getRequest();
        // 从 request 获取参数 a
        String a = request.getParameter("a");
        // 如果参数 a 为空
        if (StringUtils.isBlank(a)) {
            LOGGER.info(">>>>>>>>>>>>>>>> 参数 a 为空！ <<<<<<<<<<<<<<<<");
            // 禁止路由，禁止访问下游服务
            requestContext.setSendZuulResponse(false);
            // 设置 responseBody，供 postFilter 使用
            requestContext.setResponseBody("{\"status\": 500, \"message\": \"参数 a 为空！\"}");
            // 用于下游 Filter 判断是否执行
            requestContext.set("logic-is-success", false);
            // Filter 结束
            return null;
        }
        requestContext.set("logic-is-success", true);
        return null;
    }
}
