package com.yyp.ulog.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.*;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class AppStartLog implements ApplicationListener<ApplicationEvent> {
    Logger startLog = LoggerFactory.getLogger("startLog");

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartingEvent) {
            WebApplicationType webApplicationType = ((ApplicationStartingEvent) event).getSpringApplication().getWebApplicationType();
            System.out.println(String.format("*********************[%s]应用程序开始启动*********************", webApplicationType));
        } else if (event instanceof ApplicationEnvironmentPreparedEvent) {
            log("应用程序开始准备环境");
        } else if (event instanceof ApplicationPreparedEvent) {
            log("应用程序开始加载IOC");
        } else if (event instanceof ServletWebServerInitializedEvent) {
            log("应用程序容器启动完成");
        } else if (event instanceof ApplicationReadyEvent) {
            log("应用程序加载完成");
        } else if (event instanceof ContextClosedEvent && ((ContextClosedEvent) event).getApplicationContext().getParent() == null) {
            log("应用程序关闭");
        } else if (event instanceof ApplicationFailedEvent) {
            log("应用程序启动失败");
        }
    }

    private void log(String desc) {
        System.out.println();
        startLog.info("********************************{}********************************", desc);
    }
}
