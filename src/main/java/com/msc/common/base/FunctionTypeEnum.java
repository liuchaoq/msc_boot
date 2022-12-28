package com.msc.common.base;

public enum FunctionTypeEnum {
    GET("get"), UPDATE("update"), INSERT("insert"), DELETE("delete");
    // 成员变量
    private String name;

    FunctionTypeEnum(String name) {
        this.name = name;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
