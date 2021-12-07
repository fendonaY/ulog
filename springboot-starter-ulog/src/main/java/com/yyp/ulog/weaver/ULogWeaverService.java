package com.yyp.ulog.weaver;

import com.alibaba.fastjson.JSON;
import com.yyp.ulog.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

@Slf4j
public final class ULogWeaverService {
    private ULogManager uLogManager;

    private ULogFactory uLogFactory;

    public ULogWeaverService(ULogManager uLogManager, ULogFactory uLogFactory) {
        this.uLogManager = uLogManager;
        this.uLogFactory = uLogFactory;
    }

    public Object record(ULogWeaverInfo uLogWeaverInfo, BusinessHandler businessHandler) throws Throwable {
        boolean exist = uLogManager.existContext();
        if (!exist) {
            ULogContext uLogContext = new SimpleULogContext().buildContextId();
            uLogManager.buildContext(uLogContext);
            ULogInfo log = uLogFactory.createLog(uLogContext, uLogWeaverInfo);
            if (ULogWeaverInfo.WeaverValue.DEFAULT_NULL.getValue().equals(log.getSingleSign()))
                uLogWeaverInfo.setSingleSign(null);
            Assert.hasLength(uLogWeaverInfo.getSingleSign(), "no singleSign no weaving");
            uLogContext.getULogHolder().setULogInfo(log);
        }
        return doRecord(!exist, uLogWeaverInfo.isNeedResult(), businessHandler);
    }

    private Object doRecord(boolean record, boolean isNeedResult, BusinessHandler businessHandler) throws Throwable {
        try {
            Object handle = businessHandler.handle();
            try {
                if (record) {
                    if (isNeedResult)
                        uLogManager.getContext().getULogHolder().getULogInfo().setLogResult(JSON.toJSONString(handle).getBytes(StandardCharsets.UTF_8));
                    uLogManager.saveLog();
                }
            } catch (Exception e) {
                log.error("record log error：", e.getMessage());
            }
            return handle;
        } catch (Throwable t) {
            if (record) {
                try {
                    ULogInfo uLogInfo = uLogManager.getContext().getULogHolder().getULogInfo();
                    uLogInfo.setOperatorState(1);
                    uLogManager.getContext().getULogHolder().getULogInfo().setLogResult(JSON.toJSONString(t).getBytes(StandardCharsets.UTF_8));
                    uLogManager.saveLog();
                } catch (Exception e) {
                    log.error("record log error：", e.getMessage());
                }
            }
            throw t;
        } finally {
            uLogManager.removeLog();
        }
    }
}
