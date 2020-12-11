package com.example.shop.controllers;

import com.example.shop.persistence.entities.Review;
import com.example.shop.persistence.entities.Shopuser;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.dto.ReviewDto;
import com.example.shop.persistence.entities.Product;
import com.example.shop.persistence.entities.enums.Role;
import com.example.shop.services.ProductService;
import com.example.shop.services.ReviewService;
import com.example.shop.services.ShopuserService;
import com.example.shop.services.feign.clients.ShopFeignClient;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ShopFeignClient shopFeignClient;
    private final ProductService productService;
    private final ShopuserService shopuserService;

    private final AmqpTemplate amqpTemplate;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String moderateReview(@PathVariable UUID id, @RequestParam String option) {
        return "redirect:/products/" + reviewService.moderate(id, option);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public String addReview(ReviewDto reviewDto, HttpSession httpSession, Principal principal) {

        Optional<Product> productOptional = productService.getOneById(reviewDto.getProductId());
        Optional<Shopuser> shopuserOptional = shopuserService.findByPhone(principal.getName());

        Product product;

        if (productOptional.isPresent() && shopuserOptional.isPresent()) {
            product = productOptional.get();
            Review review = Review.builder()
                    .commentary(reviewDto.getCommentary())
                    .product(product)
                    .shopuser(shopuserOptional.get())
                    .approved(shopuserOptional.get().getRole().equals(Role.ROLE_ADMIN))
                    .build();

            amqpTemplate.convertAndSend("super-shop.exchange","super.shop","User " + principal.getName() +  " has left review");

            reviewService.save(review);

            return "redirect:/products/" + product.getId();
        }

        return "redirect:/";

    }

}