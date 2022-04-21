package com.yyp.ulog.core.parse;

import com.yyp.ulog.util.AnnotationUtil;
import com.yyp.ulog.weaver.ULog;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class ULogAnnotationParser implements AnnotationParser<ULogWeaverInfo> {

    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        return AnnotationUtil.existsAnnotation(targetClass, ULog.class);
    }

    @Override
    public ULogWeaverInfo parseAnnotation(AnnotatedElement element) {
        MergedAnnotation annotation = AnnotationUtil.getAnnotation(element, ULog.class);
        return parseULogAnnotation(annotation);
    }

    @Override
    public <E> AnnotationInfoProvider<E> getAnnotationInfo(Method method, Class<?> targetClass) {
        return () -> (E) parseAnnotation(method);
    }


    private ULogWeaverInfo parseULogAnnotation(MergedAnnotation uLog) {
        ULogWeaverInfo logWeaverInfo = new ULogWeaverInfo();
        if (MergedAnnotation.missing() == uLog) {
            return logWeaverInfo;
        }
        logWeaverInfo.setModule(uLog.getString("module"));
        logWeaverInfo.setType(uLog.getString("type"));
        logWeaverInfo.setDesc(uLog.getString("desc"));
        logWeaverInfo.setNeedResult(!uLog.getBoolean("ignoreResult"));
        logWeaverInfo.setNeedParam(!uLog.getBoolean("ignoreParam"));
        return logWeaverInfo;
    }
}
