package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}