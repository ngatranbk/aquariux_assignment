package com.aquariux.cryptotrading.repository;

import com.aquariux.cryptotrading.model.TradeTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
  Page<TradeTransaction> findByUserIdOrderByDtCreatedDesc(Long userId, Pageable pageable);
}
