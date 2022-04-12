package com.yyp.ulog.core.builder;

public class ThreadNameContextIdBuilder implements ContextIdBuilder {
    @Override
    public String build() {
        return Thread.currentThread().getName();
    }

}
