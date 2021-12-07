package com.yyp.ulog.weaver;

@FunctionalInterface
public interface BusinessHandler {

    Object handle() throws Throwable;
}
