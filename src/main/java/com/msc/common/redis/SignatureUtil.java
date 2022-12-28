package com.msc.common.redis;

import com.msc.common.util.RedisUtil;
import com.msc.common.weixin.TokenUtil;
import com.msc.common.weixin.WeiXinConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * @ClassName SignatureUtil
 * @DESCRIPTION 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）,
 * 有效的jsapi_ticket, timestamp（时间戳）,
 * url（当前网页的URL，不包含#及其后面部分） 。
 * 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，
 * 使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1。
 * 这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
 *
 * 即signature=sha1(string1)。 示例：
 * @AUTHOR liuCq
 * @DATE 2022/7/6 10:27
 **/
@Data
public class SignatureUtil {
    @Resource
    private RedisUtil redisUtil;

    private String noncestr;//随机字符串
    /**
     * jsapi_ticket是公众号用于调用微信 JS 接口的临时票据。
     * 正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。
     * 由于获取jsapi_ticket的 api 调用次数非常有限，频繁刷新jsapi_ticket会导致 api 调用受限，影响自身业务，
     * 开发者必须在自己的服务全局缓存jsapi_ticket 。
    **/
    private String jsapi_ticket;
    private String timestamp = "" + System.currentTimeMillis()/ 1000;
    private String url = "http://www.villageserver.cn";
    private String appId = WeiXinConstant.APP_ID;
    private String signature;

    public SignatureUtil(String ticket, String url) throws Exception {
        this.jsapi_ticket = ticket;
        this.noncestr = UUID.randomUUID().toString().replace("-", "");
        Map params = new HashMap();

        params.put("jsapi_ticket", this.jsapi_ticket);

        params.put("noncestr", this.noncestr);

        params.put("timestamp", this.timestamp);

        params.put("url", StringUtils.isEmpty(url)?this.url:url.split("#")[0]);

        Map sortParams = sortAsc(params);

//1.2 使用URL键值对的格式拼接成字符串

        String str = mapJoin(sortParams, false);

        this.signature = shaEncode(str);

    }

    private HashMap sortAsc(Map map) {

        HashMap tempMap = new LinkedHashMap();

        List<String> infoIds = new ArrayList<>(map.keySet());

//排序

        Collections.sort(infoIds, new Comparator<String> (){


            public int compare(String o1, String o2) {

                return o1.compareTo(o2);

            }

        });

        for (int i = 0; i < infoIds.size(); i++) {

            String key = infoIds.get(i);

            tempMap.put(key, map.get(key));

        }

        return tempMap;

    }

    private static String mapJoin(Map<String, String> map, boolean valueUrlEncode) {

        StringBuilder sb = new StringBuilder();

        for (String key : map.keySet()) {

            if (map.get(key) != null && !"".equals(map.get(key))) {

                try {

                    String temp = (key.endsWith("_") && key.length() > 1) ? key.substring(0, key.length() - 1) : key;

                    sb.append(temp);

                    sb.append("=");

//获取到map的值

                    String value = map.get(key);

//判断是否需要url编码

                    if (valueUrlEncode) {

                        value = URLEncoder.encode(map.get(key), "utf-8").replace("+", "%20");

                    }

                    sb.append(value);

                    sb.append("&");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();

                }

            }

        }

        if (sb.length() > 0) {

            sb.deleteCharAt(sb.length() - 1);

        }

        return sb.toString();

    }


    private String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @Override
    public String toString() {
        return "SignatureUtil{" +
                "redisUtil=" + redisUtil +
                ", noncestr='" + noncestr + '\'' +
                ", jsapi_ticket='" + jsapi_ticket + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", url='" + url + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        try {
        System.out.println(new SignatureUtil("LIKLckvwlJT9cWIhEQTwfJt0Wc-D5rnHR2tRa2Uh_mH9FHLdAH5lsgRXop_2kBy0Ycx5XYwqpZBUfS8Pxsk6Iw","").toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
