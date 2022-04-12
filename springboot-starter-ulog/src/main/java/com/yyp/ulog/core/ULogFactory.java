package com.yyp.ulog.core;

import com.yyp.ulog.weaver.ULogWeaverInfo;

public interface ULogFactory<T extends ULogInfo> {

    T createLog(ULogContext uLogContext, ULogWeaverInfo weaverInfo);
}
