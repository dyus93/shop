package com.example.shop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop.persistence.entities.Product;
import com.example.shop.persistence.entities.Review;
import com.example.shop.persistence.entities.Shopuser;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByProduct(Product product);
    List<Review> findByShopuser(Shopuser shopuser);
}