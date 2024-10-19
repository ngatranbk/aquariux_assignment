package com.aquariux.cryptotrading.validator;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;

public class CryptoTradingValidator {
  public static boolean isCryptoSymbolValid(String cryptoSymbol) {
    return CryptoSymbolEnum.contains(cryptoSymbol);
  }
}
