package com.aquariux.cryptotrading.dto;

import com.aquariux.cryptotrading.model.TradeTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeTransactionDto {
  private String cryptoSymbol;
  private String txnType;
  private BigDecimal amount;
  private BigDecimal price;
  private BigDecimal totalPrice;
  private LocalDateTime dtCreated;

  public static TradeTransactionDto fromTradeTransaction(TradeTransaction tradeTransaction) {
    TradeTransactionDto dto = new TradeTransactionDto();
    dto.setCryptoSymbol(tradeTransaction.getCryptoSymbol().name());
    dto.setTxnType(tradeTransaction.getTxnType().name());
    dto.setAmount(tradeTransaction.getAmount());
    dto.setPrice(tradeTransaction.getPrice());
    dto.setTotalPrice(tradeTransaction.getTotalPrice());
    dto.setDtCreated(tradeTransaction.getDtCreated());
    return dto;
  }
}
