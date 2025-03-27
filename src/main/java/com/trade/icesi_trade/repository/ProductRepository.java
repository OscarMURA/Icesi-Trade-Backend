package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

            Product findByProduct_Id(Long product_id);
            Product findByProduct_Name(String title);
            List<Product> findByCategory_Id(Long category_id);
            List<Product> findBySeller_Id (Long sellerId);
            List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
            List<Product> findByLocation(String location);
            List<Product> findByProduct_TitleContaining(String title);
            Page <Product> findAll(Pageable pageable);

}