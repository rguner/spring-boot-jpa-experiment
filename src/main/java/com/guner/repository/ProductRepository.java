package com.guner.repository;

import com.guner.entity.Product;
import com.guner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //Optional<Product> findByTitleAndPrice(String title, int price);

    // yukarıdaki satır ile aynı işi yapar
    @Query("select p from Product p where p.title = ?1 and p.price = ?2")
    Optional<Product> findByTitleAndPrice(String title, int price);
}
