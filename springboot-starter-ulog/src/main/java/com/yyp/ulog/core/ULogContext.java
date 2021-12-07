package com.yyp.ulog.core;

/**
 * 日志上下文
 */
public interface ULogContext {

    ULogContext buildContextId();

    ULogHolder getULogHolder();

    String getContextId();

    void formatDesc(String... params);
}
