package com.uf.store.util;

public class ParamsValueUtil {
  public static Float getFloadValue(String value){
      return StringUtil.isNullOrEmpty(value)?null:Float.valueOf(value);
  }
}
