package com.trade.icesi_trade.Service.Interface;

import com.trade.icesi_trade.model.FavoriteProduct;
import java.util.List;

public interface FavoriteProductService {
    List<FavoriteProduct> get();

    FavoriteProduct addFavoriteProduct(FavoriteProduct favoriteProduct);

    boolean removeFavoriteProduct(Long userId, Long productId);

    FavoriteProduct getFavoriteProduct(Long userId, Long productId);

    List<FavoriteProduct> getFavoriteProductsByUser(Long userId);

    void delete(Long id);

    FavoriteProduct findByUserIdAndProductId(Long userId, Long productId);
}
