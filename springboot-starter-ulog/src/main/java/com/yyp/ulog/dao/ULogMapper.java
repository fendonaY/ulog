package com.yyp.ulog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyp.ulog.core.ULogInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ULogMapper extends BaseMapper<ULogInfo> {
}
