package com.yyp.ulog;

import com.yyp.ulog.core.DBULogManager;
import com.yyp.ulog.core.DefaultULogFactory;
import com.yyp.ulog.core.ULogFactory;
import com.yyp.ulog.core.ULogManager;
import com.yyp.ulog.listener.AppStartLog;
import com.yyp.ulog.weaver.ULogWeaverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class ULogConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = ULogManager.class)
    public ULogManager dbULogManager() {
        return new DBULogManager();
    }

    @Bean
    @ConditionalOnMissingBean(value = ULogFactory.class)
    public ULogFactory defaultFactory() {
        return new DefaultULogFactory();
    }

    @Bean
    public ULogWeaverService uLogWeaverService(@Autowired ULogManager uLogManager, @Autowired ULogFactory uLogFactory) {
        return new ULogWeaverService(uLogManager, uLogFactory);
    }

    @Bean
    public ULogScanner ulogScanner(@Autowired ULogWeaverService weaverService) {
        return new ULogScanner(weaverService);
    }
}
