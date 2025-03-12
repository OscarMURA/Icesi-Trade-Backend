package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}