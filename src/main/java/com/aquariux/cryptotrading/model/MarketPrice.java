package com.aquariux.cryptotrading.model;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MARKET_PRICE")
public final class MarketPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private CryptoSymbolEnum cryptoSymbol;

  private BigDecimal bidPrice;
  private BigDecimal bidQty;
  private BigDecimal askPrice;
  private BigDecimal askQty;
  private LocalDateTime dtReceived;
}
