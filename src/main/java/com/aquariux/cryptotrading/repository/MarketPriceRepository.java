package com.aquariux.cryptotrading.repository;

import com.aquariux.cryptotrading.model.MarketPrice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    @Modifying
    @Query(value = "MERGE INTO crypto.market_price (crypto_symbol, bid_price, ask_price, dt_received) " +
            "KEY (crypto_symbol) " +
            "VALUES (:cryptoSymbol, :bidPrice, :askPrice, :dtReceived)",
            nativeQuery = true)
    @Transactional
    void upsertMarketPrice(@Param("cryptoSymbol") String cryptoSymbol,
                           @Param("bidPrice") BigDecimal bidPrice,
                           @Param("askPrice") BigDecimal askPrice,
                           @Param("dtReceived") LocalDateTime dtReceived);
}
