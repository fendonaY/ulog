package com.yyp.ulog.core.parse;

@FunctionalInterface
public interface AnnotationInfoProvider<T> {

    T getAnnotationInfo();
}
