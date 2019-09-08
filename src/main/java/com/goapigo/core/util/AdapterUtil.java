package com.goapigo.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class AdapterUtil {

  public static Class<?> getType(Field field) {
    if (!(field.getGenericType() instanceof ParameterizedType)) {
      return field.getType();
    }
    ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
    return (Class<?>) stringListType.getActualTypeArguments()[0];
  }
}
