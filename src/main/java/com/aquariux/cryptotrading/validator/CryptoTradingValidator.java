package com.aquariux.cryptotrading.validator;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.constants.TxnTypeEnum;

public class CryptoTradingValidator {
  public static boolean isCryptoSymbolValid(String cryptoSymbol) {
    return CryptoSymbolEnum.contains(cryptoSymbol);
  }

  public static boolean isTxnTypeValid(String cryptoSymbol) {
    return TxnTypeEnum.contains(cryptoSymbol);
  }
}
