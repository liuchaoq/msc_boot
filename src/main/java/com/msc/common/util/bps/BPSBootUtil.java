package com.msc.common.util.bps;


import java.util.Map;

/**
 * @Description: TODO
 * @author: edison
 * @date: 2021年05月13日 18:18
 */
public class BPSBootUtil {
    //访问的ip地址，该地址为eos服务地址,自动加http://前缀
    private static String ipAddress;
    //访问的端口，eos服务端口
    private static int port;
    //eos的服务名称
    private static String serverName;
    //旧的restful接口ip地址
    private static String oldIpAddress;

    public static String getIpAddress() {
        return ipAddress;
    }

    public static void setIpAddress(String ipAddress) {
        BPSBootUtil.ipAddress = ipAddress;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        BPSBootUtil.port = port;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        BPSBootUtil.serverName = serverName;
    }

    /**
     * 描述: 构造http方式get请求url, 如：http://ip:port/serverName/restful/query?key=value
     * 时间: 2019/8/16 10:52
     * 作者: tianqiang
     * @param: [queryUrl, paramMap]
     * @return: java.lang.String
     **/
    public static String buildGetHttpRequestUrl(String queryUrl, Map<String, Object> paramMap)  {
        StringBuilder url = new StringBuilder();
        //获取url，如：http://ipAddress:port/serverName/queryurl
        url.append(getHttpQueryUrl(queryUrl));

        String params = buildGetRequestParam(paramMap);
        if(params.length() > 0) {
            url.append("?").append(params);
        }
        return url.toString();
    }

    /**
     * 描述: 获取http查询url，格式：https://ipAddress:port/serverName/queryurl
     * 时间: 2019/8/16 10:50
     * 作者: tianqiang
     * @param: [queryUrl]
     * @return: java.lang.String
     **/
    public static String getHttpQueryUrl(String queryUrl) {
        StringBuilder url = new StringBuilder();
        //拼接地址 http://ipAddress:port/serverName
        url.append(ipAddress).append(":").append(port).append("/").append(serverName);

        queryUrl = queryUrl.endsWith("/") ? queryUrl.substring(0, queryUrl.lastIndexOf("/")-1) : queryUrl;
        queryUrl = queryUrl.startsWith("/") ? queryUrl : "/" + queryUrl;

        url.append(queryUrl);
        return url.toString();
    }

    /**
     * 描述: 构造get请求入参，按key=vlaue&的形式拼接
     * 时间: 2019/8/15 15:25
     * 作者: tianqiang
     *
     * @throws:
     * @param: [paramMap]
     * @return: java.lang.String
     **/
    private static String buildGetRequestParam(Map<String, Object> paramMap) {
        StringBuilder param = new StringBuilder();
        if(paramMap == null || paramMap.size() == 0){
            return param.toString();
        }
        //拼接key=vlaue&的形式
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            param.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (param.length() > 1) {
            param = param.deleteCharAt(param.length() - 1);
        }
        return param.toString();
    }
}
