package com.msc.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @ClassName: LongAdapter
 * @Description: TODO Long 适配器
 * @author liuyuxiao
 * @date 2021年5月31日
 *
 */
public class LongAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

  @Override
  public Long deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
    return arg0.getAsLong();
  }

  @Override
  public JsonElement serialize(Long arg0, Type arg1, JsonSerializationContext arg2) {
    String value = "";
    if (arg0 != null) {
      value = String.valueOf(arg0);
    }
    return new JsonPrimitive(value);
  }

}
