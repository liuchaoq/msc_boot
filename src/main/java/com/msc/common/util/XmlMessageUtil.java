package com.msc.common.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName XmlMessageUtil
 * @DESCRIPTION
 * @AUTHOR liuCq
 * @DATE 2022/6/6 17:07
 **/
@Slf4j
public class XmlMessageUtil {
    public static String getMessage(HttpServletRequest request) {
        StringBuffer reqXmlData = new StringBuffer();
        try {
            InputStream inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((s = in.readLine()) != null) {
                reqXmlData.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("流解析xml数据异常!");
            e.printStackTrace();
        }
        //判断请求数据是否为空
        if (reqXmlData.length() <= 0) {
            System.out.println("请求数据为空!");
        }
        log.error("收到微信消息内容:"+reqXmlData.toString());
        //json类型数据
        JSONObject jsonObject = XML.toJSONObject(reqXmlData.toString());
        return jsonObject.toString();
    }

    public static String getXmlMessage(HttpServletRequest request) {
        StringBuffer reqXmlData = new StringBuffer();
        try {
            InputStream inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((s = in.readLine()) != null) {
                reqXmlData.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("流解析xml数据异常!");
            e.printStackTrace();
        }
        //判断请求数据是否为空
        if (reqXmlData.length() <= 0) {
            System.out.println("请求数据为空!");
        }
        log.error("收到微信消息内容:"+reqXmlData.toString());
        //json类型数据
        return reqXmlData.toString();
    }
}
