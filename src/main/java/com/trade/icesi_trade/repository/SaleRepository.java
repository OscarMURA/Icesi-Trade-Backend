package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Buscar ventas por vendedor (seller_id)
    List<Sale> findByBuyer_Id(Long buyerId);

    // Buscar ventas por producto (product_id)
    List<Sale> findByProduct_Id(Long productId);

    // Contar ventas de un vendedor
    long countByBuyer_Id(Long sellerId);

    // Buscar ventas de un producto específico y un comprador específico
    List<Sale> findByProduct_IdAndBuyer_Id(Long productId, Long buyerId);
}
