package com.msc.common.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentDate = new Date();
        //创建时间默认当前时间
        setFieldValByName("ts", currentDate,metaObject);
        setFieldValByName("validFlag", "Y",metaObject);
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        Date currentDate = new Date();
        //修改时间
        setFieldValByName("ts",currentDate,metaObject);
    }

}
