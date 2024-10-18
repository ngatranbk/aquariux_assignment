package com.aquariux.cryptotrading.repository;

import com.aquariux.cryptotrading.model.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {

}
