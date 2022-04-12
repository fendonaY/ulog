package com.yyp.ulog.core;

import com.yyp.ulog.core.builder.ContextIdBuilder;
import com.yyp.ulog.weaver.ULogWeaverInfo;

/**
 * 日志上下文
 */
public class SimpleULogContext implements ULogContext {

    private String contextId;

    private ULogHolder uLogHolder;

    private ContextIdBuilder contextIdBuilder;

    public SimpleULogContext() {
        this.uLogHolder = new ULogHolder();
    }

    public SimpleULogContext(LogGlobalConfig logGlobalConfig, ULogWeaverInfo uLogWeaverInfo) {
        this.contextIdBuilder = logGlobalConfig.getContextIdBuilder();
        this.uLogHolder = new ULogHolder(logGlobalConfig.getLogExecutor(), logGlobalConfig.getLogHandler());
        this.uLogHolder.setULogWeaverInfo(uLogWeaverInfo);
        this.contextId = contextIdBuilder.build();
    }

    @Override
    public ULogHolder getULogHolder() {
        return this.uLogHolder;
    }

    @Override
    public String getContextId() {
        return this.contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }
}
