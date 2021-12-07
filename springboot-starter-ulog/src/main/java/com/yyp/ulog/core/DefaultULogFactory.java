package com.yyp.ulog.core;

import com.alibaba.fastjson.JSONArray;
import com.yyp.ulog.weaver.ULogWeaverInfo;
import org.springframework.beans.BeanUtils;

import java.nio.charset.StandardCharsets;

public class DefaultULogFactory implements ULogFactory {

    @Override
    public ULogInfo createLog(ULogContext uLogContext, ULogWeaverInfo weaverInfo) {
        ULogInfo uLogInfo = new ULogInfo();
        BeanUtils.copyProperties(weaverInfo, uLogInfo);
//        uLogInfo.setCreateAt();
        uLogInfo.setOperatorId(uLogContext.getContextId());
        uLogInfo.setLogParam(JSONArray.toJSONString(weaverInfo.getArguments()).getBytes(StandardCharsets.UTF_8));
        return uLogInfo;
    }
}
