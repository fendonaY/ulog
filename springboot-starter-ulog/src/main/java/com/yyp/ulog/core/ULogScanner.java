package com.yyp.ulog.core;

import cn.hutool.core.util.ReflectUtil;
import com.yyp.ulog.core.parse.AnnotationParser;
import com.yyp.ulog.core.parse.ULogAnnotationParser;
import com.yyp.ulog.interceptor.ULogInterceptor;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import com.yyp.ulog.weaver.ULogWeaverService;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author yyp
 * @description:
 * @date 2021/4/618:06
 */
public final class ULogScanner extends AbstractAutoProxyCreator {

    private MethodInterceptor interceptor;

    private ULogWeaverService uLogWeaverService;

    private LogGlobalConfig logGlobalConfig;

    private AnnotationParser<ULogWeaverInfo> uLogAnnotationParser = new ULogAnnotationParser();

    public ULogScanner() {
    }

    public ULogScanner(ULogWeaverService uLogWeaverService, LogGlobalConfig logGlobalConfig) {
        this.uLogWeaverService = uLogWeaverService;
        this.logGlobalConfig = logGlobalConfig;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class beanClass, String beanName, TargetSource customTargetSource)
            throws BeansException {
        if (interceptor == null)
            return null;
        return new Object[]{interceptor};
    }

    @SneakyThrows
    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        interceptor = null;
        Class<?> target = AopProxyUtils.ultimateTargetClass(bean);

        Method[] methods = ReflectUtil.getMethods(target);
        for (Method method : methods) {
            ULogWeaverInfo uLogWeaverInfo = uLogAnnotationParser.parseAnnotation(method);
            if (StringUtils.hasText(uLogWeaverInfo.getType())) {
                logGlobalConfig.getULogWeaverInfoMap().put(method, uLogWeaverInfo);
                interceptor = new ULogInterceptor(uLogWeaverService, this.logGlobalConfig);
            }
        }

        if (interceptor == null) {
            return bean;
        }
        setProxyTargetClass(true);
        return super.wrapIfNecessary(bean, beanName, cacheKey);
    }

}
