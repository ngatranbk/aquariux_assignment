package com.aquariux.cryptotrading.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TradeRequestDto {
  private Long userId;
  private String cryptoSymbol;
  private String txnType;
  private BigDecimal amount;
}
