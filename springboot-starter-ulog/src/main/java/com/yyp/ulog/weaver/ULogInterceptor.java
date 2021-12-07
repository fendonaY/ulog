package com.yyp.ulog.weaver;

import com.yyp.ulog.util.AnnotationUtil;
import com.yyp.ulog.util.ParamUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author yyp
 * @description:
 * @date 2021/4/618:06
 */
public class ULogInterceptor implements MethodInterceptor {

    private ULogWeaverService logWeaverService;

    public ULogInterceptor(ULogWeaverService logWeaverService) {
        this.logWeaverService = logWeaverService;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method specificMethod = ClassUtils.getMostSpecificMethod(methodInvocation.getMethod(), AopUtils.getTargetClass(methodInvocation.getThis()));
        final Method method = BridgeMethodResolver.findBridgedMethod(specificMethod);
        MergedAnnotation uLog = AnnotationUtil.getAnnotation(method, ULog.class);
        if (uLog != MergedAnnotation.missing()) {
            ULogWeaverInfo logWeaverInfo = parseULogAnnotation(uLog);
            prepareWeaverInfo(logWeaverInfo, methodInvocation);
            getSingleSign(logWeaverInfo);
            return logWeaverService.record(logWeaverInfo, methodInvocation::proceed);
        } else {
            return methodInvocation.proceed();
        }
    }

    private void getSingleSign(ULogWeaverInfo logWeaverInfo) {
        if (logWeaverInfo.getSingleSignIndex() != -1) {
            Object attr = ParamUtil.getAttr(logWeaverInfo.getSingleSignIndex(), "", logWeaverInfo.getArguments());
            logWeaverInfo.setSingleSign(String.valueOf(attr));
        } else {
            Object attrList = ParamUtil.getAttrList(logWeaverInfo.getSingleSign(), logWeaverInfo.getArguments());
            if (attrList != null && attrList instanceof List) {
                attrList = ((List) attrList).get(0);
            }
            logWeaverInfo.setSingleSign(String.valueOf(attrList));
        }

    }

    private ULogWeaverInfo parseULogAnnotation(MergedAnnotation uLog) {
        ULogWeaverInfo logWeaverInfo = new ULogWeaverInfo();
        logWeaverInfo.setSingleSignIndex(uLog.getInt("singleSignIndex"));
        logWeaverInfo.setBusModule(uLog.getString("busModule"));
        logWeaverInfo.setType(uLog.getString("type"));
        logWeaverInfo.setSingleSign(uLog.getString("singleSign"));
        logWeaverInfo.setDesc(uLog.getString("desc"));
        logWeaverInfo.setNeedResult(!uLog.getBoolean("ignoreResult"));
        return logWeaverInfo;
    }

    private void prepareWeaverInfo(ULogWeaverInfo logWeaverInfo, MethodInvocation methodInvocation) {
        logWeaverInfo.setArguments(methodInvocation.getArguments());
        logWeaverInfo.setTargetMethod(methodInvocation.getMethod());
        logWeaverInfo.setTargetObject(methodInvocation.getThis());
    }
}
