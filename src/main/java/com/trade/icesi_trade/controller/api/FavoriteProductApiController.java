package com.trade.icesi_trade.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.trade.icesi_trade.Service.Interface.FavoriteProductService;
import com.trade.icesi_trade.dtos.FavoriteProductDto;
import com.trade.icesi_trade.mappers.FavoriteProductMapper;
import com.trade.icesi_trade.model.FavoriteProduct;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
@Tag(name = "Favorite Products", description = "Operaciones sobre productos favoritos")
public class FavoriteProductApiController {

    @Autowired
    private FavoriteProductService favoriteProductService;

    @Autowired
    private FavoriteProductMapper favoriteProductMapper;

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<FavoriteProductDto>> getAll(@RequestParam(required = false) Long userId) {
        List<FavoriteProductDto> products;
        
        if (userId != null) {
            products = favoriteProductService.getFavoriteProductsByUser(userId)
                    .stream()
                    .map(favoriteProductMapper::entityToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(products);
        } else {
            products = favoriteProductService.get()
                .stream()
                .map(favoriteProductMapper::entityToDto)
                .collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Toggle favorite product")
    public ResponseEntity<FavoriteProductDto> toggleFavorite(@Valid @RequestBody FavoriteProductDto dto) {
        System.out.println("🔍 DTO RECIBIDO: userId=" + dto.getUserId() + ", productId=" + dto.getProductId());

        if (dto.getUserId() == null || dto.getProductId() == null) {
            throw new IllegalArgumentException("MALPARIDA VIDA");
        }
        FavoriteProduct existing = favoriteProductService.findByUserIdAndProductId(dto.getUserId(), dto.getProductId());

        if (existing != null) {
            favoriteProductService.delete(existing.getId());
            return ResponseEntity.noContent().build();
        }

        FavoriteProduct created = favoriteProductService.addFavoriteProduct(favoriteProductMapper.dtoToEntity(dto));
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
