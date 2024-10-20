package com.aquariux.cryptotrading.constants;

import lombok.Getter;

@Getter
public enum TradeErrorMessage {
  OK("Trade successfully"),
  MARKET_PRICE_NOT_FOUND("Market Price Not Found"),
  INSUFFICIENT_STOCK_AMOUNT("Insufficient Stock Amount"),
  INSUFFICIENT_USDT("Insufficient USDT"),
  INSUFFICIENT_BTCUSDT("Insufficient Bitcoin"),
  INSUFFICIENT_ETHUSDT("Insufficient Ethereum");

  private final String message;

  TradeErrorMessage(String message) {
    this.message = message;
  }
}
