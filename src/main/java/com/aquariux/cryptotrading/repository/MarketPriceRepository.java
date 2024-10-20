package com.aquariux.cryptotrading.repository;

import com.aquariux.cryptotrading.model.MarketPrice;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
  @Modifying
  @Query(
      value =
          "MERGE INTO crypto.market_price (crypto_symbol, bid_price, bid_qty, ask_price, ask_qty, dt_received) "
              + "KEY (crypto_symbol) "
              + "VALUES (:cryptoSymbol, :bidPrice, :bidQty, :askPrice, :askQty, :dtReceived)",
      nativeQuery = true)
  @Transactional
  void upsertMarketPrice(
      @Param("cryptoSymbol") String cryptoSymbol,
      @Param("bidPrice") BigDecimal bidPrice,
      @Param("bidQty") BigDecimal bidQty,
      @Param("askPrice") BigDecimal askPrice,
      @Param("askQty") BigDecimal askQty,
      @Param("dtReceived") LocalDateTime dtReceived);

  @Query(
      value = "SELECT * FROM crypto.market_price WHERE crypto_symbol = :cryptoSymbol",
      nativeQuery = true)
  MarketPrice findMarketPricesByCryptoSymbol(@Param("cryptoSymbol") String cryptoSymbol);
}
