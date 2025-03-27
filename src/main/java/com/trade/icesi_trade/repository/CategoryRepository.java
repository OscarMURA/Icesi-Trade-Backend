package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
        
        Category findByName(String name);
        Optional<Category> findById(Long id_category);
}