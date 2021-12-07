package com.yyp.ulog.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("u_log")
public class ULogInfo {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String api;

    private String operator;

    private String singleSign;

    private String busModule;

    private String type;

    private Integer operatorState = 0;

    private String operatorId;

    @TableField(value = "`desc`")
    private String desc;

    private String date;

    private String createAt;

    private byte[] logParam;

    private byte[] logResult;

}
