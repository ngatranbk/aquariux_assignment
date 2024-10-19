package com.aquariux.cryptotrading.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketPriceElement {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
}