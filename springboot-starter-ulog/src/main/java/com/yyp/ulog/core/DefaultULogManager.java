package com.yyp.ulog.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;

@Slf4j
public class DefaultULogManager implements ULogManager {

    private ThreadLocal<ULogContext> context = new NamedThreadLocal<>("logContext");

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
    public boolean saveLog(Object result) {
        if (!existContext())
            return false;
        ULogContext context = getContext();
        return context.getULogHolder().saveLog(result);
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
