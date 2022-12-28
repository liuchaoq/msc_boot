package com.msc.common.weixin.entity;

import lombok.Data;

/**
 * @ClassName WxTemplateMessageDetailDTO
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/18 16:15
 **/
@Data
public class WxTemplateMessageDetail {
    //推送顶部消息
    private WxTemplateMessageDetailContent first;
    private WxTemplateMessageDetailContent keyword1;
    private WxTemplateMessageDetailContent keyword2;
    private WxTemplateMessageDetailContent keyword3;
    private WxTemplateMessageDetailContent keyword4;
    private WxTemplateMessageDetailContent remark;
}
