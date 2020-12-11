package com.example.shop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.persistence.entities.Shopuser;

import java.util.Optional;
import java.util.UUID;

public interface ShopuserRepository extends JpaRepository<Shopuser, UUID> {
    Optional<Shopuser> findByPhone(String phone);
    boolean existsByPhone(String phone);
}
