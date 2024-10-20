package com.aquariux.cryptotrading.service;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.constants.TradeErrorMessage;
import com.aquariux.cryptotrading.constants.TxnTypeEnum;
import com.aquariux.cryptotrading.dto.TradeTransactionDto;
import com.aquariux.cryptotrading.model.MarketPrice;
import com.aquariux.cryptotrading.model.TradeTransaction;
import com.aquariux.cryptotrading.model.User;
import com.aquariux.cryptotrading.repository.MarketPriceRepository;
import com.aquariux.cryptotrading.repository.TradeTransactionRepository;
import com.aquariux.cryptotrading.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TradingService {
  @Autowired private UserRepository userRepository;

  @Autowired private MarketPriceRepository marketPriceRepository;

  @Autowired private TradeTransactionRepository transactionRepository;

  public TradeErrorMessage tradeCrypto(
      Long userId, CryptoSymbolEnum cryptoSymbol, TxnTypeEnum txnType, BigDecimal amount) {
    User user = userRepository.findById(userId).orElseThrow();
    MarketPrice latestPrice =
        marketPriceRepository.findMarketPricesByCryptoSymbol(cryptoSymbol.name());
    if (latestPrice == null) {
      // the market price was not updated
      return TradeErrorMessage.MARKET_PRICE_NOT_FOUND;
    }
    BigDecimal unitPrice;
    BigDecimal stockAmount;
    if (TxnTypeEnum.BUY.equals(txnType)) {
      unitPrice = latestPrice.getAskPrice();
      stockAmount = latestPrice.getAskQty();
    } else {
      unitPrice = latestPrice.getBidPrice();
      stockAmount = latestPrice.getBidQty();
    }

    if (stockAmount.compareTo(amount) < 0) {
      // buy or sell more than current valid amount of crypto at server
      return TradeErrorMessage.INSUFFICIENT_STOCK_AMOUNT;
    }

    BigDecimal totalPrice = unitPrice.multiply(amount);
    if (TxnTypeEnum.BUY.equals(txnType) && user.getUsdtBalance().compareTo(totalPrice) < 0) {
      // not enough money to buy
      return TradeErrorMessage.INSUFFICIENT_USDT;
    }
    if (TxnTypeEnum.BUY.equals(txnType)) {
      if (CryptoSymbolEnum.BTCUSDT.equals(cryptoSymbol)) {
        user.setBtcBalance(user.getBtcBalance().add(amount));
      }
      if (CryptoSymbolEnum.ETHUSDT.equals(cryptoSymbol)) {
        user.setEthBalance(user.getEthBalance().add(amount));
      }
      user.setUsdtBalance(user.getUsdtBalance().subtract(totalPrice));
    } else {
      if (CryptoSymbolEnum.BTCUSDT.equals(cryptoSymbol)) {
        if (user.getBtcBalance().compareTo(amount) < 0) {
          // not enough Bitcoin to sell
          return TradeErrorMessage.INSUFFICIENT_BTCUSDT;
        }
        user.setBtcBalance(user.getBtcBalance().subtract(amount));
      }
      if (CryptoSymbolEnum.ETHUSDT.equals(cryptoSymbol)) {
        if (user.getEthBalance().compareTo(amount) < 0) {
          // not enough Ethereum to sell
          return TradeErrorMessage.INSUFFICIENT_ETHUSDT;
        }
        user.setEthBalance(user.getEthBalance().subtract(amount));
      }
      user.setUsdtBalance(user.getUsdtBalance().add(totalPrice));
    }

    userRepository.save(user);
    TradeTransaction transaction = new TradeTransaction();
    transaction.setUser(user);
    transaction.setCryptoSymbol(cryptoSymbol);
    transaction.setTxnType(txnType);
    transaction.setAmount(amount);
    transaction.setPrice(unitPrice);
    transaction.setTotalPrice(totalPrice);
    transaction.setDtCreated(LocalDateTime.now());
    transactionRepository.save(transaction);

    return TradeErrorMessage.OK;
  }

  public Page<TradeTransactionDto> getTradeHistoryByUser(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<TradeTransaction> transactions =
        transactionRepository.findByUserIdOrderByDtCreatedDesc(userId, pageable);
    return transactions.map(TradeTransactionDto::fromTradeTransaction);
  }
}
