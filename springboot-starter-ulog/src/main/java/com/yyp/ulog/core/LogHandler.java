package com.yyp.ulog.core;

@FunctionalInterface
public interface LogHandler {

    void handle(ULogInfo uLogInfo) throws Throwable;
}
