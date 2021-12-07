package com.yyp.ulog.core;

import com.yyp.ulog.weaver.ULogWeaverInfo;

public interface ULogFactory {

    ULogInfo createLog(ULogContext uLogContext, ULogWeaverInfo weaverInfo);
}
