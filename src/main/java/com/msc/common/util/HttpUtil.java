package com.msc.common.util;

import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author
 * @ClassName: HttpUtil
 * @Description: TODO 请求工具
 * @date
 */
@Slf4j
public class HttpUtil {

    /**
     * @Description: TODO(这里用一句话描述这个方法的作用) @param: @param jsonObj @param: @param url @param: @param token @param: @return @return: EcomResultDO<Object> @author: james @throws
     */
    public static String httpPost(String jsonStr, String url, String token) {
        String result = null;
        HttpPost post = null;
        HttpClient httpClient = HttpClients.createDefault();
        String remoteUrl = url;
        try {
            post = new HttpPost(remoteUrl);
            // 构造消息头
            post.setHeader("Content-type", "application/json; charset=utf-8");
            if (token != null && !token.equals("")) {
                post.setHeader("Authorization", "Basic " + token);
            }
            log.error(token);
            // 构建消息实体
            StringEntity entity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            log.error(response.getStatusLine().toString());
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity he = response.getEntity();
                String respContent = EntityUtils.toString(he, "UTF-8");
                return respContent;
            } else {

                result = "HTTP POST 请求异常错误码[" + statusCode + "]";
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            result = e.getMessage();
            return result;
        }
    }

    public static String httpGet(String req_url) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(req_url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (ConnectException ce) {
            log.error("server connection timed out.");
        } catch (Exception e) {
            String fullStackTrace = ExceptionUtils.getStackTrace(e);
            log.error("https request error:{}" + fullStackTrace);
        }
        return buffer.toString();
    }

    public static String httpXml(String requestUrl, String requestData) {
        String requestMethod = "POST";
        String requestContentType = "application/xml";
        String requestCharset = "UTF-8";

        log.info("发送给 {}的请求报文为 {}", requestUrl, requestData);
        String result = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", requestContentType);
            connection.connect();

            // 将发送的数据进行写入
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestData.getBytes(requestCharset));
            }

            log.info("第二部分：处理返回的数据========================================");
            // 获取服务端处理后的返回报文：
            // 1.获取输入流
            // 2.封装为一个BufferedReader
            // 3.读取数据写入Java缓存
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), requestCharset);
                    sbf.append(lines);
                }
                log.info("接收到服务端返回的数据为：{}", sbf.toString());
                result = sbf.toString();
            }
            connection.disconnect();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
