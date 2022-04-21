package com.yyp.ulog.interceptor;

import com.yyp.ulog.core.LogGlobalConfig;
import com.yyp.ulog.core.parse.AnnotationParser;
import com.yyp.ulog.core.parse.ULogAnnotationParser;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import com.yyp.ulog.weaver.ULogWeaverService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * ulog切面
 */
public class ULogInterceptor implements MethodInterceptor {

    private ULogWeaverService logWeaverService;

    private LogGlobalConfig logGlobalConfig;

    private AnnotationParser<ULogWeaverInfo> uLogAnnotationParser = new ULogAnnotationParser();

    public ULogInterceptor(ULogWeaverService logWeaverService, LogGlobalConfig logGlobalConfig) {
        this.logWeaverService = logWeaverService;
        this.logGlobalConfig = logGlobalConfig;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Class<?> targetClass = AopUtils.getTargetClass(methodInvocation.getThis());
        Method specificMethod = ClassUtils.getMostSpecificMethod(methodInvocation.getMethod(), targetClass);
        final Method method = BridgeMethodResolver.findBridgedMethod(specificMethod);

        ULogWeaverInfo uLogWeaverInfo = this.logGlobalConfig.getULogWeaverInfoMap().get(method);
        if (uLogWeaverInfo == null) {
            uLogWeaverInfo = uLogAnnotationParser.parseAnnotation(method);
        }
        if (uLogWeaverInfo != null && StringUtils.hasText(uLogWeaverInfo.getType())) {
            logGlobalConfig.getULogWeaverInfoMap().put(method, uLogWeaverInfo);
            prepareWeaverInfo(uLogWeaverInfo, methodInvocation);
            return logWeaverService.record(uLogWeaverInfo, methodInvocation::proceed);
        } else {
            return methodInvocation.proceed();
        }
    }

    private void prepareWeaverInfo(ULogWeaverInfo logWeaverInfo, MethodInvocation methodInvocation) {
        logWeaverInfo.setArguments(methodInvocation.getArguments());
        logWeaverInfo.setTargetMethod(methodInvocation.getMethod());
        logWeaverInfo.setTargetObject(methodInvocation.getThis());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        logWeaverInfo.setRequest(requestAttributes.getRequest());
    }
}
