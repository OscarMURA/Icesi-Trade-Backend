package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

}