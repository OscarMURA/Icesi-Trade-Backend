package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    Product findByTitle(String title);

    List<Product> findByCategory_Id(Long category_id);

    List<Product> findBySeller_Id(Long sellerId);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByLocation(String location);

    List<Product> findByTitleContaining(String title);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.isSold = false OR p.isSold IS NULL) AND p.seller.id != :sellerId")
    List<Product> findAvailableProductsExcludingSeller(@Param("sellerId") Long sellerId);

}