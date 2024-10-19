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
public class HuobiPriceElement {
  private String symbol;
  private BigDecimal bid;
  private BigDecimal bidSize;
  private BigDecimal ask;
  private BigDecimal askSize;
}
