package com.yyp.ulog.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@Slf4j
public class AppStartLog implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartingEvent) {
            WebApplicationType webApplicationType = ((ApplicationStartingEvent) event).getSpringApplication().getWebApplicationType();
            System.out.println(String.format("*********************[%s]应用程序开始启动*********************", webApplicationType));
        } else if (event instanceof ApplicationEnvironmentPreparedEvent) {
            printLog("应用程序开始准备环境");
        } else if (event instanceof ApplicationPreparedEvent) {
            printLog("应用程序开始加载IOC");
        } else if (event instanceof ApplicationReadyEvent) {
            printLog("应用程序加载完成");
        } else if (event instanceof ContextClosedEvent && ((ContextClosedEvent) event).getApplicationContext().getParent() == null) {
            printLog("应用程序关闭");
        } else if (event instanceof ApplicationFailedEvent) {
            printLog("应用程序启动失败");
        }
    }

    private void printLog(String desc) {
        System.out.println();
        log.info("*********************{}*********************", desc);
    }
}
