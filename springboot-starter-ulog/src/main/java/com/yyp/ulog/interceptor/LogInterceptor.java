package com.yyp.ulog.interceptor;

import com.yyp.ulog.core.LogGlobalConfig;
import com.yyp.ulog.core.ULogInfo;
import com.yyp.ulog.servlet.RequestWrapper;
import com.yyp.ulog.servlet.ResponseWrapper;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局日志拦截器
 */
public class LogInterceptor implements HandlerInterceptor {

    private LogGlobalConfig logGlobalConfig;

    public LogInterceptor(LogGlobalConfig logGlobalConfig) {
        this.logGlobalConfig = logGlobalConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ULogInfo uLogInfo = mockRequestLog(request);
        logGlobalConfig.getPrintHeadInfo().accept(uLogInfo);
        logGlobalConfig.getPrintRequestInfo().accept(uLogInfo);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws IOException {
        logGlobalConfig.getPrintRequestInfo().accept(mockResultLog(request, response));
    }

    private ULogInfo mockRequestLog(HttpServletRequest request) {
        ULogInfo uLogInfo = new ULogInfo();
        ULogWeaverInfo uLogWeaverInfo = new ULogWeaverInfo();
        uLogWeaverInfo.setRequest(request);
        RequestWrapper requestWrapper = new RequestWrapper(request);
        uLogInfo.setULogWeaverInfo(uLogWeaverInfo);
        uLogInfo.setLogParam(requestWrapper.getRequestBody());
        uLogInfo.setApi(request.getRequestURI());
        return uLogInfo;
    }

    private ULogInfo mockResultLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ULogInfo uLogInfo = new ULogInfo();
        ULogWeaverInfo uLogWeaverInfo = new ULogWeaverInfo();
        uLogInfo.setULogWeaverInfo(uLogWeaverInfo);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        uLogInfo.setLogResult(responseWrapper.getResponseData());
        uLogInfo.setOperatorState(0);
        uLogInfo.setApi(request.getRequestURI());
        return uLogInfo;
    }

}
