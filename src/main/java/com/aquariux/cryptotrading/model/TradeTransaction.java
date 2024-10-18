package com.aquariux.cryptotrading.model;

import com.aquariux.cryptotrading.constants.CryptoPairEnum;
import com.aquariux.cryptotrading.constants.TxnTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "TRADE_TRANSACTION")
public final class TradeTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CryptoPairEnum cryptoPair;

    @Enumerated(EnumType.STRING)
    private TxnTypeEnum txnType;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime timestamp;
}
