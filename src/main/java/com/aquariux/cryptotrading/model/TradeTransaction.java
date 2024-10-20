package com.aquariux.cryptotrading.model;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.constants.TxnTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Entity(name = "TRADE_TRANSACTION")
public final class TradeTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private CryptoSymbolEnum cryptoSymbol;

  @Enumerated(EnumType.STRING)
  private TxnTypeEnum txnType;

  @NotNull
  @Column(nullable = false)
  private BigDecimal amount;

  @NotNull
  @Column(nullable = false)
  private BigDecimal price;

  @NotNull
  @Column(nullable = false)
  private BigDecimal totalPrice;

  private LocalDateTime dtCreated;
}
