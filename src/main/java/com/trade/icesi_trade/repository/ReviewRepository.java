package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByReview_Id(Long id);
    List<Review> findByProduct_Id(Long productId);
    List<Review> findByReviewer_Id(Long reviewerId);
    List<Review> findByReviewee_Id(Long revieweeId);
    List<Review> findByProduct_IdAndRatingGreaterThanEqual(Long productId, Integer rating);
    List<Review> findByProduct_IdAndRatingBetween(Long productId, Integer minRating, Integer maxRating);
    long countByProduct_Id(Long productId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
}
