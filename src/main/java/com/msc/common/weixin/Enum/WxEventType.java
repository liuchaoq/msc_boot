package com.msc.common.weixin.Enum;

/**
 * @ClassName WxEventType
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/7 9:58
 **/
public enum WxEventType {
    VIEW("VIEW",1)
    , CLICK("CLICK",2)
    ,LOCATION("LOCATION",3)
    ,SCAN("SCAN",4)
    ,SUBSCRIBE("subscribe",5)
    ,UNSUBSCRIBE("unsubscribe",5);
    private Integer index;
    private String typeName;
    private WxEventType(String typeName, Integer index) {
        this.typeName = typeName;
        this.index = index;
    }
    public String getName() {
        return this.typeName;
    }
}
