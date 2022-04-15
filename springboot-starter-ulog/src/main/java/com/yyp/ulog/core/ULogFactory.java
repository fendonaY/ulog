package com.yyp.ulog.core;

import com.yyp.ulog.weaver.ULogWeaverInfo;

/**
 * 创建日志抽象工厂
 */
public interface ULogFactory<T extends ULogInfo> {

    /**
     * 根据编织信息，创建日志信息。
     *
     * @return 日志信息
     */
    T createLog(ULogContext uLogContext, ULogWeaverInfo weaverInfo);
}
