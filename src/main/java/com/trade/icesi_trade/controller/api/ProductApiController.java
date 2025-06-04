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

import com.trade.icesi_trade.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

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
    private ProductMapper productMapper;

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductDto>> getAll() {
        List<ProductDto> products = productService.getAllProducts()
                .stream()
                .map(productMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.entityToDto(product));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String userEmail;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            // fallback o error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.findUserByEmail(userEmail);

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
}
