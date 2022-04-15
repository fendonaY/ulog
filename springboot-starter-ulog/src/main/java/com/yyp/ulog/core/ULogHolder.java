package com.yyp.ulog.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

@Data
@Slf4j
public class ULogHolder {

    private ULogInfo uLogInfo;

    private ULogWeaverInfo uLogWeaverInfo;

    private ExecutorService logExecutor;

    private LogHandler logHandler;

    public ULogHolder() {
    }

    public ULogHolder(ExecutorService logExecutor, LogHandler logHandler) {
        this.logExecutor = logExecutor;
        this.logHandler = logHandler;
    }

    public void formatDesc(String... params) {
        if (this.uLogInfo != null) {
            FormattingTuple formattingTuple = MessageFormatter.arrayFormat(getULogInfo().getLogDesc(), params);
            getULogInfo().setLogDesc(formattingTuple.getMessage());
        }
    }

    public boolean saveLog(Object result) {
        if (getULogInfo() == null)
            return false;
        if (uLogWeaverInfo.isNeedParam()) {
            getULogInfo().setLogParam(JSONArray.toJSONString(uLogWeaverInfo.getArguments()).getBytes(StandardCharsets.UTF_8));
        }
        if (uLogWeaverInfo.isNeedResult()) {
            getULogInfo().setLogResult(JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8));
        }
        if (result instanceof Throwable) {
            getULogInfo().setOperatorState(1);
            getULogInfo().setThrowable((Throwable) result);
        }

        getLogExecutor().submit(() -> {
            try {
                Assert.notNull(logHandler, "logHandler is null");
                logHandler.handle(getULogInfo());
                log.info("weaving log succeeded, contextId：{}", uLogInfo.getLogId());
            } catch (Throwable e) {
                log.error("ULog contextId：" + uLogInfo.getLogId() + "  weaving error：{}", e);
            }
        });
        return true;
    }
}
