package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.FavoriteProductService;
import com.trade.icesi_trade.dtos.FavoriteProductDto;
import com.trade.icesi_trade.mappers.FavoriteProductMapper;
import com.trade.icesi_trade.model.FavoriteProduct;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
@Tag(name = "Favorite Products", description = "Operaciones sobre productos favoritos")
public class FavoriteProductApiController {

    @Autowired
    private FavoriteProductService favoriteProductService;

    @Autowired
    private FavoriteProductMapper favoriteProductMapper;

    @Operation(summary = "Add a product to favorites")
    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<FavoriteProductDto> addFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        FavoriteProduct created = favoriteProductService.addFavoriteProduct(userId, productId);
        return ResponseEntity.ok(favoriteProductMapper.entityToDto(created));
    }

    @Operation(summary = "Remove a product from favorites")
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        boolean deleted = favoriteProductService.removeFavoriteProduct(userId, productId);
        return deleted ?
                ResponseEntity.ok("Producto eliminado de favoritos.") :
                ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get a specific favorite product")
    @GetMapping("/{userId}/{productId}")
    public ResponseEntity<FavoriteProductDto> getFavorite(@PathVariable @NotNull Long userId,
                                                          @PathVariable @NotNull Long productId) {
        FavoriteProduct favorite = favoriteProductService.getFavoriteProduct(userId, productId);
        System.out.println("Favorite: " + favorite);
        return ResponseEntity.ok(favoriteProductMapper.entityToDto(favorite));
    }

    @Operation(summary = "Get all favorite products for a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteProductDto>> getFavoritesByUser(@PathVariable Long userId) {
        List<FavoriteProductDto> favorites = favoriteProductService.getFavoriteProductsByUser(userId)
                .stream()
                .map(favoriteProductMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(favorites);
    }
}
