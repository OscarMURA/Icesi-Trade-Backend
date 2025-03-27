package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
            ImageProduct findByProduct_Id(Long product_id);
            ImageProduct findByImage_Id(Long image_id);
}