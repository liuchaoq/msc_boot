package com.msc.common.weixin.entity;

import lombok.Data;

/**
 * @ClassName WxTemplateMessageDetailContentDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/18 16:18
 **/
@Data
public class WxTemplateMessageDetailContent {
    private String value;
    private String color = "#173177";

    public WxTemplateMessageDetailContent(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public WxTemplateMessageDetailContent(String value) {
        this.value = value;
    }
}
