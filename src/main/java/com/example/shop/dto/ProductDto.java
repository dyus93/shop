package com.example.shop.dto;

import com.example.shop.persistence.entities.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String title;
    private String description;
    private Double price;
    private boolean available;
    private ProductCategory category;
}