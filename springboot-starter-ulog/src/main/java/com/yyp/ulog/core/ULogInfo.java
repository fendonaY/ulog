package com.yyp.ulog.core;

import lombok.Data;

@Data
public class ULogInfo {

    /**
     * id
     */
    private Integer id;

    /**
     * 请求api
     */
    private String api;

    /**
     * 请求唯一标识
     */
    private String singleSign;

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

}
