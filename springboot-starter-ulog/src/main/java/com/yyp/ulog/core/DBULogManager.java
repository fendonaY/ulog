package com.yyp.ulog.core;

import com.yyp.ulog.dao.ULogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DBULogManager implements ULogManager {

    @Resource
    @Lazy
    private ULogMapper uLogMapper;

    private ThreadLocal<ULogContext> context = new ThreadLocal();

    private ExecutorService logExecutor = new ThreadPoolExecutor(60, 80, 5L, TimeUnit.SECONDS, new SynchronousQueue<>());

    @Override
    public ULogContext getContext() {
        ULogContext uLogContext = context.get();
        if (uLogContext == null)
            return new SimpleULogContext();
        return uLogContext;
    }

    @Override
    public boolean existContext() {
        return context.get() != null;
    }

    @Override
    public boolean saveLog() {
        if (!existContext())
            return false;
        ULogContext context = getContext();
        ULogInfo uLogInfo = context.getULogHolder().getULogInfo();
        if (uLogInfo.getId() != null)
            return false;
        logExecutor.submit(() -> {
            try {
                uLogMapper.insert(uLogInfo);
                log.info("weaving log succeeded, contextId：{}", context.getContextId());
            } catch (Exception e) {
                log.error("ULog contextId：" + context.getContextId() + "  weaving error：{}", e);
            }
        });
        return true;
    }

    @Override
    public void removeLog() {
        context.remove();
    }

    @Override
    public void buildContext(ULogContext uLogContext) {
        context.set(uLogContext);
    }
}
