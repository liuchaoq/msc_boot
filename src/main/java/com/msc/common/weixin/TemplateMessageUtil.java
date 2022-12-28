package com.msc.common.weixin;

import com.alibaba.fastjson.JSON;
import com.msc.common.constant.CmsConstant;
import com.msc.common.util.HttpUtil;
import com.msc.common.util.RedisUtil;
import com.msc.common.weixin.entity.WxTemplateMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;

/**
 * @ClassName TemplateMessageUtil
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/7/18 16:23
 **/
@Slf4j
public class TemplateMessageUtil {
    @Resource
    private RedisUtil redisUtil;

    public static void sendTemplateMsg(WxTemplateMessage wxTemplateMessage, String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        try {
            HttpUtil.httpPost(JSON.toJSONString(wxTemplateMessage), url+token, null);
        } catch (Exception e) {
            log.error("推送模板消息报错,推送内容：{},错误原因:{}",JSON.toJSONString(wxTemplateMessage),e.getMessage());
        }
    }
}
