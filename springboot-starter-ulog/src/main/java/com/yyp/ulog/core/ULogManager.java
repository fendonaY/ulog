package com.yyp.ulog.core;

public interface ULogManager {

    /**
     * 获取日志上下文
     *
     * @return 当前日志的上下文
     */
    ULogContext getContext();

    /**
     * 当前是否存在日志上下文
     *
     * @return true存在，false不存在
     */
    boolean existContext();

    /**
     * 日志上下文加入日志管理器中
     */
    void buildContext(ULogContext uLogContext);

    /**
     * 持久化当前上下文的日志信息
     *
     * @param result 业务响应值
     * @return true保存成功，false保存失败
     */
    boolean saveLog(Object result);

    /**
     * 删除日志
     */
    void removeLog();


}
