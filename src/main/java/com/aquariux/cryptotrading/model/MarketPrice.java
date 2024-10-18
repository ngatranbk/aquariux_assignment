package com.aquariux.cryptotrading.model;

import com.aquariux.cryptotrading.constants.CryptoPairEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "MARKET_PRICE")
public final class MarketPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CryptoPairEnum cryptoPair;

    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private LocalDateTime timestamp;
}
