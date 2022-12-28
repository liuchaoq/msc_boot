package com.msc.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: JsonUtil
 * @Description: TODO json 工具
 * @author liuyuxiao
 * @date 2021年5月31日
 *
 */
public final class JsonUtil {

    /**
     * @Fields gson : TODO 初始化 序列化工具
     */
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Long.class, new LongAdapter())
            .registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapter(BigDecimal.class, new BigDecimalAdapter())
            .create();

    /**
     * @Description: TODO 将 Object 序列化为 Json 字符串
     * @param @param obj
     * @param @return
     * @return
     * @throws
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * @Description: TODO 将 Json 字符串序列化成 Object
     * @param @param str
     * @param @param clazz
     * @param @return
     * @return
     * @throws
     */
    public static Object fromJson(String str, Class<?> clazz) {
        return gson.fromJson(str, clazz);
    }

    public static <T> T fromJson(String str, Type type) {
        return gson.fromJson(str, type);
    }
}
