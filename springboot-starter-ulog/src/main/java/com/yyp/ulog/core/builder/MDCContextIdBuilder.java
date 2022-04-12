package com.yyp.ulog.core.builder;

import org.slf4j.MDC;

public class MDCContextIdBuilder implements ContextIdBuilder {

    private String contextIDKey = "traceId";

    @Override
    public String build() {
        return MDC.get(getContextIDKey());
    }

    public String getContextIDKey() {
        return contextIDKey;
    }

    public void setContextIDKey(String contextIDKey) {
        this.contextIDKey = contextIDKey;
    }
}
