package com.msc.common.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.alibaba.fastjson.JSON;
import com.msc.common.vo.Result;
import com.msc.user.dto.jgmessage.AuthCodeDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName JgMessageUtil
 * @DESCRIPTION 极光短信服务
 * @AUTHOR liuCq
 * @DATE 2022/6/27 16:37
 **/
@Slf4j
public class JgMessageUtil {
    //
    private final static String APP_KEY = "d565473dfd1771fc61e7613d";
    //
    private final static String MASTER_SECRET = "68ab7b8573b4a96fe02020b5";
    //验证码模板id
    private final static Integer AUTH_CODE_TEMPLATE_ID = 1;
    //短信签名id
    private final static Integer SIGN_ID = 23515;

    public static Result<Object> sendAuthCOde(AuthCodeDTO authCodeDTO) {
        SMSClient client = new SMSClient(MASTER_SECRET, APP_KEY);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(authCodeDTO.getMobile())
                .setTempId(AUTH_CODE_TEMPLATE_ID)
                .addTempPara("code", authCodeDTO.getCode().toString())
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            if (res.isResultOK()) {
                log.info(res.getMessageId());
                return Result.OK();
            } else {
                return Result.error(res.toString());
            }
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Message: " + e.getMessage());
            return Result.error(e.getErrorMessage());
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return Result.error(e.getLocalizedMessage());
        }
    }

}
