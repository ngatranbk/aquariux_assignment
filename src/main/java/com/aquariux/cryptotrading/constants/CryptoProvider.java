package com.aquariux.cryptotrading.constants;

import lombok.Getter;

@Getter
public enum CryptoProvider {
    BINANCE("https://api.binance.com/api/v3/ticker/bookTicker"),
    HUOBI("https://api.huobi.pro/market/tickers");

    private final String url;

    CryptoProvider(String url) {
        this.url = url;
    }
}
