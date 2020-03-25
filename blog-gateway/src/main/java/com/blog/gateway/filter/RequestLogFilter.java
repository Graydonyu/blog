package com.blog.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.annotation.Order;
import org.springframework.util.StreamUtils;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

@Order(1)
@WebFilter(filterName="RequestLogFilter", urlPatterns="/*")
public class RequestLogFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;//要打印返回信息，必须得用"post"
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            InputStream in = request.getInputStream();
            String reqBody = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            // 打印请求方法，路径
            RequestLogFilter.LOGGER
                    .info("request url:\t" + request.getMethod() + "\t" + request.getRequestURL().toString());
            Map<String, String[]> map = request.getParameterMap();
            // 打印请求url参数
            if (map != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("request parameters:\t");
                for (Map.Entry<String, String[]> entry : map.entrySet()) {
                    sb.append("[" + entry.getKey() + "=" + printArray(entry.getValue()) + "]");
                }
                RequestLogFilter.LOGGER.info(sb.toString());
            }
            // 打印请求json参数
            if (reqBody != null) {
                RequestLogFilter.LOGGER.info("request body:\t" + reqBody);
            }

            // 打印response
            InputStream out = ctx.getResponseDataStream();
            String outBody = StreamUtils.copyToString(out, Charset.forName("UTF-8"));
            if (outBody != null) {
                RequestLogFilter.LOGGER.info("response body:\t" + outBody);
            }

            ctx.setResponseBody(outBody);//重要！！！

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    String printArray(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
