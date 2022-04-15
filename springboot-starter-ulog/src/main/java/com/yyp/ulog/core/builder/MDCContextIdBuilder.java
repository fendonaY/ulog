package com.yyp.ulog.core.builder;

import org.slf4j.MDC;

public class MDCContextIdBuilder implements ContextIdBuilder {

    private static String contextIDKey = "traceId";

    @Override
    public String build() {
        return MDC.get(getContextIDKey());
    }

    public static String getContextIDKey() {
        return MDCContextIdBuilder.contextIDKey;
    }

    public static void setContextIDKey(String contextIDKey) {
        MDCContextIdBuilder.contextIDKey = contextIDKey;
    }
}
