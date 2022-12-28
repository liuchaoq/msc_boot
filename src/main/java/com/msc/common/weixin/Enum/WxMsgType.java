package com.msc.common.weixin.Enum;

public enum WxMsgType {
    EVENT("event",1)
    ,TEXT("text",2)
    ,IMAGE("image",3)
    ,VOICE("voice",4)
    ,VIDEO("video",5)
    ,SHORTVIDEO("shortvideo",6)
    ,LOCATION("location",7)
    ,LINK("link",8);
    private Integer index;
    private String typeName;

    private WxMsgType(String typeName, int index) {
        this.typeName = typeName;
        this.index = index;
    }
    public String getName() {
        return typeName;
    }
}
