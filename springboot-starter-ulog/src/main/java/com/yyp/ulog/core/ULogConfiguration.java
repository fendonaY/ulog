package com.yyp.ulog.core;

import com.yyp.ulog.weaver.ULogWeaverService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class ULogConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ULogManager defaultULogManager() {
        return new DefaultULogManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public ULogFactory defaultFactory() {
        return new DefaultULogFactory();
    }

    @Bean
    public ULogWeaverService uLogWeaverService(ULogManager uLogManager, ULogFactory uLogFactory, LogGlobalConfig logGlobalConfig) {
        return new ULogWeaverService(uLogManager, uLogFactory, logGlobalConfig);
    }

    @Bean
    public ULogScanner ulogScanner(ULogWeaverService weaverService, LogGlobalConfig logGlobalConfig) {
        return new ULogScanner(weaverService, logGlobalConfig);
    }

    @Bean
    public LogGlobalConfig logGlobalConfig() {
        return new LogGlobalConfig();
    }

}
