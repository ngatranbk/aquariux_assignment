package com.aquariux.cryptotrading.constants;

import java.util.Arrays;

public enum TxnTypeEnum {
  BUY,
  SELL;

  public static boolean contains(String value) {
    if (value == null) {
      return false;
    }
    return Arrays.stream(TxnTypeEnum.values()).anyMatch(e -> e.name().equalsIgnoreCase(value));
  }

  public static TxnTypeEnum from(String value) {
    if (value == null) {
      return null;
    }
    return TxnTypeEnum.valueOf(value.toUpperCase());
  }
}
