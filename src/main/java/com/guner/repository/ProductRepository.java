package com.guner.repository;

import com.guner.entity.Product;
import com.guner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByTitleAndPrice(String title, int price);
}
