package com.yyp.ulog.weaver;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface ULog {

    /**
     * 操作类型
     */
    String type();

    /**
     * 唯一标识符对应下标
     * 从参数里获取
     */
    int singleSignIndex() default -1;

    /**
     * 唯一标识符
     * 从参数里获取
     */
    String singleSign() default "";

    /**
     * 功能模板
     */
    String busModule();

    /**
     * 忽略返回值
     *
     * @return false 记录返回值，true 忽略
     */
    boolean ignoreResult() default false;

    /**
     * 操作描述
     * 支持format
     * <p>例：‘你{}吗’ -> ’你好吗‘</p>
     */
    String desc();

}
