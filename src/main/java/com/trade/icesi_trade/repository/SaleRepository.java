package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySeller_Id(Long sellerId);
    List<Sale> findByProduct_Id(Long productId);
    long countBySeller_Id(Long sellerId);
    List<Sale> findByProduct_IdAndBuyer_Id(Long productId, Long buyerId);
}
