package com.yyp.ulog.core;

import cn.hutool.core.date.DateUtil;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import org.springframework.beans.BeanUtils;

public class DefaultULogFactory implements ULogFactory<ULogInfo> {

    @Override
    public ULogInfo createLog(ULogContext uLogContext, ULogWeaverInfo weaverInfo) {
        ULogInfo uLogInfo = new ULogInfo();
        BeanUtils.copyProperties(weaverInfo, uLogInfo);
        uLogInfo.setLogDesc(weaverInfo.getDesc());
        uLogInfo.setLogId(uLogContext.getContextId());
        uLogInfo.setApi(weaverInfo.getTargetObject().getClass().getName());
        uLogInfo.setCreateAt(DateUtil.now());
        uLogInfo.setULogWeaverInfo(weaverInfo);
        return uLogInfo;
    }
}
