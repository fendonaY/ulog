package com.yyp.ulog.weaver;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ULogWeaverInfo {

    protected enum WeaverValue {
        DEFAULT_NULL("null");

        private String value;

        WeaverValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String operator;

    private String type;

    private int singleSignIndex;

    private String singleSign;

    private String busModule;

    private String desc;

    private boolean needResult;

    private Object targetObject;

    private Method targetMethod;

    private Object[] arguments;

}
