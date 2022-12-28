package com.msc.common.util;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalAdapter implements JsonSerializer<BigDecimal>, JsonDeserializer<BigDecimal> {

  @Override
  public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (StringUtils.isNotBlank(json.getAsString())) {
    	return json.getAsBigDecimal();
    }
    return new BigDecimal(0);
  }

  @Override
  public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString());
  }

}
