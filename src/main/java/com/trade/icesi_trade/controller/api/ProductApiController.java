package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.ProductService;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.dtos.ProductDto;
import com.trade.icesi_trade.mappers.ProductMapper;
import com.trade.icesi_trade.model.Product;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.trade.icesi_trade.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import com.trade.icesi_trade.Service.blob.AzureBlobService;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
@Tag(name = "Products", description = "CRUD operations for products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private AzureBlobService blobService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String search) {

        // Obtener el usuario autenticado para excluir sus productos
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
            User user = userService.findUserByEmail(userEmail);
            if (user != null) {
                currentUserId = user.getId();
            }
        }

        List<ProductDto> products;

        if (sellerId != null) {
            products = productService.getProductsBySellerId(sellerId)
                    .stream()
                    .map(productMapper::entityToDto)
                    .collect(Collectors.toList());
        } else {
            // Si no se especifica sellerId, obtener productos disponibles excluyendo los
            // del usuario actual
            if (currentUserId != null) {
                products = productService.getAvailableProductsExcludingSeller(currentUserId)
                        .stream()
                        .map(productMapper::entityToDto)
                        .collect(Collectors.toList());
            } else {
                // Si no hay usuario autenticado, obtener todos los productos disponibles
                products = productService.getAllProducts()
                        .stream()
                        .filter(p -> p.getIsSold() == null || !p.getIsSold())
                        .map(productMapper::entityToDto)
                        .collect(Collectors.toList());
            }
        }

        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategoryId().equals(categoryId))
                    .collect(Collectors.toList());
        }

        if (minPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getStatus().toLowerCase().equals(status.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (location != null && !location.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getLocation().toLowerCase().contains(location.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getTitle().toLowerCase().contains(searchLower) ||
                            p.getDescription().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.entityToDto(product));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available products excluding current seller")
    public ResponseEntity<List<ProductDto>> getAvailableProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        String userEmail;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Product> products = productService.getAvailableProductsExcludingSeller(user.getId());

        List<ProductDto> productDtos = products.stream()
                .map(productMapper::entityToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDtos);
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si la autenticación es null
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        Object principal = authentication.getPrincipal();

        String userEmail;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        User user = userService.findUserByEmail(userEmail);

        // Verificar si el usuario existe
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Product product = productMapper.dtoToEntity(dto);
        product.setSeller(user);

        Product created = productService.createProduct(product);

        return new ResponseEntity<>(productMapper.entityToDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        Product updated = productService.updateProduct(id, productMapper.dtoToEntity(dto));
        return ResponseEntity.ok(productMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = blobService.uploadImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir imagen: " + e.getMessage());
        }
    }

    @PostMapping("/upload-images")
    public ResponseEntity<List<String>> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) {
        try {
            // Validar límite de 3 imágenes
            if (files.length > 3) {
                return ResponseEntity.badRequest()
                        .body(Collections.emptyList());
            }

            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String imageUrl = blobService.uploadImage(file);
                imageUrls.add(imageUrl);
            }
            return ResponseEntity.ok(imageUrls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PatchMapping("/{id}/sold")
    @Operation(summary = "Mark product as sold")
    public ResponseEntity<ProductDto> markAsSold(@PathVariable Long id) {
        Product product = productService.markProductAsSold(id);
        return ResponseEntity.ok(productMapper.entityToDto(product));
    }

    @PostMapping("/test-delete-image")
    @Operation(summary = "Test endpoint to delete a specific image from Azure")
    public ResponseEntity<String> testDeleteImage(@RequestParam String imageUrl) {
        try {
            boolean result = blobService.deleteImageByUrl(imageUrl);
            return ResponseEntity.ok("Resultado de eliminación: " + result + " para URL: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar imagen: " + e.getMessage());
        }
    }
}
