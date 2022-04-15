package com.yyp.ulog.weaver;

import com.yyp.ulog.core.*;
import com.yyp.ulog.core.builder.MDCContextIdBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.concurrent.Executors;

@Slf4j
public final class ULogWeaverService implements InitializingBean {
    private ULogManager uLogManager;

    private ULogFactory uLogFactory;

    private LogGlobalConfig logGlobalConfig;

    public ULogWeaverService() {
    }

    public ULogWeaverService(ULogManager uLogManager, ULogFactory uLogFactory, LogGlobalConfig logGlobalConfig) {
        this.uLogManager = uLogManager;
        this.uLogFactory = uLogFactory;
        this.logGlobalConfig = logGlobalConfig;
    }

    public Object record(ULogWeaverInfo uLogWeaverInfo, BusinessHandler businessHandler) throws Throwable {
        boolean exist = uLogManager.existContext();
        if (!exist) {
            ULogContext uLogContext = new SimpleULogContext(logGlobalConfig, uLogWeaverInfo);
            uLogManager.buildContext(uLogContext);

            ULogInfo uLogInfo = uLogFactory.createLog(uLogContext, uLogWeaverInfo);
            uLogContext.getULogHolder().setULogInfo(uLogInfo);
        }
        return doRecord(!exist, businessHandler);
    }

    private Object doRecord(boolean record, BusinessHandler businessHandler) throws Throwable {
        try {
            Object handle = businessHandler.handle();
            try {
                if (record) {
                    uLogManager.saveLog(handle);
                }
            } catch (Exception e) {
                log.error("record log error：", e.getMessage());
            }
            return handle;
        } catch (Throwable t) {
            if (record) {
                try {
                    uLogManager.saveLog(t);
                } catch (Exception e) {
                    log.error("record log error：", e.getMessage());
                }
            }
            throw t;
        } finally {
            if (record)
                uLogManager.removeLog();
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(logGlobalConfig, "u-log config is null");
        logGlobalConfig.nonnullOrElseSet(logGlobalConfig::getLogExecutor, logGlobalConfig::setLogExecutor, Executors.newSingleThreadExecutor());
        logGlobalConfig.nonnullOrElseSet(logGlobalConfig::getContextIdBuilder, logGlobalConfig::setContextIdBuilder, new MDCContextIdBuilder());
        logGlobalConfig.nonnullOrElseSet(logGlobalConfig::getLogHandler, logGlobalConfig::setLogHandler, (info) -> {
            if (logGlobalConfig.getContextIdBuilder() instanceof MDCContextIdBuilder)
                MDC.put(((MDCContextIdBuilder) logGlobalConfig.getContextIdBuilder()).getContextIDKey(), info.getLogId());
            ULogWeaverInfo uLogWeaverInfo = info.getULogWeaverInfo();
            if (uLogWeaverInfo != null) {
                if (uLogWeaverInfo.isNeedParam()) {
                    logGlobalConfig.getPrintHeadInfo().accept(info);
                    logGlobalConfig.getPrintRequestInfo().accept(info);
                }
                if (uLogWeaverInfo.isNeedResult())
                    logGlobalConfig.getPrintResponseInfo().accept(info);
            }
        });
    }
}
