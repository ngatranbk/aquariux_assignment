package com.aquariux.cryptotrading.constants;

import java.util.Arrays;

public enum CryptoSymbolEnum {
  BTCUSDT,
  ETHUSDT;

  public static boolean contains(String value) {
    if (value == null) {
      return false;
    }
    return Arrays.stream(CryptoSymbolEnum.values()).anyMatch(e -> e.name().equalsIgnoreCase(value));
  }
}
