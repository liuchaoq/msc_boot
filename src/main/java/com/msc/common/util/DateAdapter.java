package com.msc.common.util;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @ClassName: DateAdapter
 * @Description: TODO 日期适配器
 * @author liuyuxiao
 * @date 2021年5月31日
 *
 */
public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (StringUtils.isNumeric(json.getAsString())) {
      return new Date(Long.valueOf(json.getAsString()));
    } else {
      return DateUtil.Str2D(json.getAsString());
    }
  }

  @Override
  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
    String value = "";
    if (src != null) {
      value = DateUtil.Date2Str(src);
    }
    return new JsonPrimitive(value);
  }

}
