package com.yyp.ulog.core;

@FunctionalInterface
public interface LogHandler {

    Object handle() throws Throwable;
}
