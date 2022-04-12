package com.yyp.ulog.core;

import com.yyp.ulog.core.builder.ContextIdBuilder;
import lombok.Data;

import java.util.concurrent.ExecutorService;

@Data
public class LogGlobalConfig {

    private LogHandler logHandler;

    private ExecutorService logExecutor;

    private ContextIdBuilder contextIdBuilder;
}
