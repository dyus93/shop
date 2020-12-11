package com.example.shop.services;

import com.example.shop.persistence.entities.Product;
import com.example.shop.persistence.entities.Review;
import com.example.shop.persistence.entities.Shopuser;
import com.example.shop.persistence.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewsByShopuser(Shopuser shopuser) {
        return reviewRepository.findByShopuser(shopuser);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public UUID moderate(UUID id, String option) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if(reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setApproved(option.equals("approve"));
            save(review);
            return review.getProduct().getId();
        } else {
            return null;
        }
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

}