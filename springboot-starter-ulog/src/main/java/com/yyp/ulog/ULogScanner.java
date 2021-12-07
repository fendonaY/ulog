package com.yyp.ulog;

import com.yyp.ulog.util.AnnotationUtil;
import com.yyp.ulog.weaver.ULog;
import com.yyp.ulog.weaver.ULogInterceptor;
import com.yyp.ulog.weaver.ULogWeaverService;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

/**
 * @author yyp
 * @description:
 * @date 2021/4/618:06
 */
public final class ULogScanner extends AbstractAutoProxyCreator {

    private MethodInterceptor interceptor;

    private ULogWeaverService uLogWeaverService;

    public ULogScanner(ULogWeaverService uLogWeaverService) {
        this.uLogWeaverService = uLogWeaverService;
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
        if (!AnnotationUtil.existsAnnotation(target, ULog.class)) {
            return bean;
        }
        interceptor = new ULogInterceptor(uLogWeaverService);
        return super.wrapIfNecessary(bean, beanName, cacheKey);
    }

}
