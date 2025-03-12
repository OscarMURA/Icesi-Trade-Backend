package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}