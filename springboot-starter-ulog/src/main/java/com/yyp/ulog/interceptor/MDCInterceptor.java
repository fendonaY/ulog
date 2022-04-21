package com.yyp.ulog.interceptor;

import cn.hutool.core.util.IdUtil;
import com.yyp.ulog.core.builder.MDCContextIdBuilder;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于MDC的链路跟踪拦截器
 */
public class MDCInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logTraceId(request, response);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    private void logTraceId(HttpServletRequest request, ServletResponse response) {
        String traceId = request.getHeader(MDCContextIdBuilder.getContextIDKey());
        if (!StringUtils.hasText(traceId)) {
            traceId = IdUtil.simpleUUID();
        }
        MDC.put(MDCContextIdBuilder.getContextIDKey(), traceId);
        ((HttpServletResponse) response).setHeader(MDCContextIdBuilder.getContextIDKey(), traceId);
    }
}
