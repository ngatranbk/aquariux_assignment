package com.aquariux.cryptotrading.dto;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.model.MarketPrice;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketPriceDto {
  @Enumerated(EnumType.STRING)
  private CryptoSymbolEnum cryptoSymbol;

  private BigDecimal bidPrice;
  private BigDecimal bidQty;
  private BigDecimal askPrice;
  private BigDecimal askQty;

  public static MarketPriceDto fromMarketPrice(MarketPrice marketPrice) {
    return new MarketPriceDto(
        marketPrice.getCryptoSymbol(),
        marketPrice.getBidPrice(),
        marketPrice.getBidQty(),
        marketPrice.getAskPrice(),
        marketPrice.getAskQty());
  }
}
