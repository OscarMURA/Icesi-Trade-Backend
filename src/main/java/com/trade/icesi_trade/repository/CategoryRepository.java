package com.trade.icesi_trade.repository;

import com.trade.icesi_trade.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}