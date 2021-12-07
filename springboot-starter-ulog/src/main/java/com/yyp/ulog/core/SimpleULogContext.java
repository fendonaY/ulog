package com.yyp.ulog.core;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 日志上下文
 */
public class SimpleULogContext implements ULogContext {

    private String contextId;

    private ULogHolder uLogHolder;

    public SimpleULogContext() {
        this.uLogHolder = new ULogHolder();
    }

    @Override
    public ULogContext buildContextId() {
        this.contextId = Thread.currentThread().getName();
        return this;
    }

    @Override
    public ULogHolder getULogHolder() {
        return this.uLogHolder;
    }

    @Override
    public String getContextId() {
        return this.contextId;
    }

    @Override
    public void formatDesc(String... params) {
        if (StringUtils.hasText(contextId)) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(uLogHolder.getULogInfo().getDesc(), params);
            uLogHolder.getULogInfo().setDesc(formattingTuple.getMessage());
        }
    }
}
