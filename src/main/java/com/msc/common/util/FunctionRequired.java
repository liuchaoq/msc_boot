package com.msc.common.util;


import com.msc.common.base.FunctionTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName LoginRequired
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/3/30 13:44
 **/
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionRequired {

    String value();

    FunctionTypeEnum type();

    public String loginUrl() default "";

}
