package com.yyp.ulog.core;

/**
 * 日志上下文
 */
public interface ULogContext {

    ULogHolder getULogHolder();

    String getContextId();
}
