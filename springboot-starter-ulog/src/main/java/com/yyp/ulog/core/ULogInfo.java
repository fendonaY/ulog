package com.yyp.ulog.core;

import com.yyp.ulog.weaver.ULogWeaverInfo;
import lombok.Data;

/**
 * ulog信息
 */
@Data
public class ULogInfo {

    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 请求api
     */
    private String api;

    /**
     * 本次请求状态：0成功，1失败
     */
    private Integer operatorState = 0;

    /**
     * 日志id
     */
    private String logId;

    /**
     * 日志描述
     */
    private String logDesc;

    /**
     * 日志创建时间
     */
    private String createAt;

    /**
     * 请求参数
     */
    private byte[] logParam;

    /**
     * 请求响应
     */
    private byte[] logResult;

    /**
     * 编织信息
     */
    private ULogWeaverInfo uLogWeaverInfo;

    /**
     * 异常信息
     */
    private Throwable throwable;

}
