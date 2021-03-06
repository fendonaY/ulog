package com.yyp.ulog.weaver;

import lombok.Data;

import javax.servlet.ServletRequest;
import java.lang.reflect.Method;

@Data
public class ULogWeaverInfo {

    private String type;

    private String module;

    private String desc;

    private boolean needResult;

    private boolean needParam;

    private Object targetObject;

    private Method targetMethod;

    private Object[] arguments;

    private ServletRequest request;

    private boolean enablePrint;

}
