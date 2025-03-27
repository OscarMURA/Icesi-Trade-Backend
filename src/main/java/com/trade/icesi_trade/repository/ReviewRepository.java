package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Buscar reseña por ID
    Review findByReview_Id(Long id);

    // Buscar todas las reseñas para un producto específico
    List<Review> findByProduct_Id(Long productId);

    // Buscar todas las reseñas hechas por un usuario específico (reviewer)
    List<Review> findByReviewer_Id(Long reviewerId);

    // Buscar todas las reseñas de un usuario específico (reviewee)
    List<Review> findByReviewee_Id(Long revieweeId);

    // Buscar reseñas para un producto con calificación mayor o igual a un valor específico
    List<Review> findByProduct_IdAndRatingGreaterThanEqual(Long productId, Integer rating);

    // Buscar reseñas por producto y calificación dentro de un rango
    List<Review> findByProduct_IdAndRatingBetween(Long productId, Integer minRating, Integer maxRating);

    // Contar las reseñas para un producto específico
    long countByProduct_Id(Long productId);

    // Calcular el promedio de calificación para un producto
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
}
