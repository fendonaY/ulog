package com.yyp.ulog.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.yyp.ulog.core.builder.ContextIdBuilder;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * ulog全局配置，提供一些扩展配置
 *
 * @see com.yyp.ulog.weaver.ULogWeaverService
 */
@Data
public class LogGlobalConfig {

    /**
     * ulog处理函数。
     * 可自定义打印或者持久化日志等其他操作
     */
    private LogHandler logHandler;

    /**
     * ulog异步处理线程池。
     */
    private ExecutorService logExecutor;

    /**
     * 日志id生成器
     */
    private ContextIdBuilder contextIdBuilder;

    /**
     * 目标与日志编织信息的映射
     */
    private Map<Method, ULogWeaverInfo> uLogWeaverInfoMap = new ConcurrentHashMap<>();

    /**
     * 开启日志同步打印功能，
     * 默认关闭，异步打印日志。
     */
    private boolean enablePrint = false;

    /**
     * 开启全局日志打印
     */
    private boolean globalPrint = false;

    /**
     * 简单的打印头部信息
     */
    private Consumer<ULogInfo> printHeadInfo = (uLogInfo) -> {
        HttpServletRequest request = (HttpServletRequest) uLogInfo.getULogWeaverInfo().getRequest();
        String uri = request.getRequestURI();
        String clientIP = ServletUtil.getClientIP(request);
        StringJoiner stringJoiner = new StringJoiner("\r\n--------------", "\r\n--------------", "");
        stringJoiner.add("client-IP：" + clientIP);
        stringJoiner.add("uri：" + uri);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String headerValue = request.getHeader(header);
            stringJoiner.add(header + "：" + headerValue);
        }
        Logger uLog = LoggerFactory.getLogger("uLog");
        uLog.trace(stringJoiner.toString());
    };

    /**
     * 简单的打印请求信息
     */
    private Consumer<ULogInfo> printRequestInfo = (uLogInfo) -> {
        String requestParamLog = StrUtil.str(uLogInfo.getLogParam(), Charset.defaultCharset());
        Logger uLog = LoggerFactory.getLogger("uLog");
        uLog.trace("api：{} logDesc：{} param：{}", uLogInfo.getApi(), uLogInfo.getLogDesc(), requestParamLog);
    };

    /**
     * 简单的打印响应信息
     */
    private Consumer<ULogInfo> printResponseInfo = (uLogInfo) -> {
        Object resultLog;
        if (uLogInfo.getOperatorState() == 0)
            resultLog = StrUtil.str(uLogInfo.getLogResult(), Charset.defaultCharset());
        else {
            resultLog = uLogInfo.getThrowable();
        }
        Logger uLog = LoggerFactory.getLogger("uLog");
        uLog.trace("api：{} response：{}", uLogInfo.getApi(), resultLog);
    };

    public <T> void nonnullOrElseSet(Supplier<T> value, Consumer<? super T> consumer, T defaultValue) {
        if (ObjectUtils.isEmpty(value.get())) {
            Optional.ofNullable(defaultValue).ifPresent(consumer);
        }
    }
}
