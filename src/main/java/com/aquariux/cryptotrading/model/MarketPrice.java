package com.aquariux.cryptotrading.model;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal askPrice;
    private LocalDateTime timestamp;
}
