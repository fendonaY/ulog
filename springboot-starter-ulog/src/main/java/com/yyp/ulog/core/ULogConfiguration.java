package com.yyp.ulog.core;

import com.yyp.ulog.filter.RequestWrapperFilter;
import com.yyp.ulog.interceptor.LogInterceptor;
import com.yyp.ulog.interceptor.MDCInterceptor;
import com.yyp.ulog.weaver.ULogWeaverService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

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


    @Configuration
    public class LogConfiguration implements WebMvcConfigurer {

        @Resource
        private LogGlobalConfig logGlobalConfig;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            if (logGlobalConfig.isGlobalPrint()) {
                registry.addInterceptor(new MDCInterceptor())
                        .addPathPatterns("/**");

                registry.addInterceptor(new LogInterceptor(this.logGlobalConfig))
                        .addPathPatterns("/**");
            }
        }

        @Bean
        public FilterRegistrationBean requestWrapperFilter() {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
            if (logGlobalConfig.isGlobalPrint()) {
                filterRegistrationBean.setFilter(new RequestWrapperFilter());
            }
            return filterRegistrationBean;
        }

    }
}
