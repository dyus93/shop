package com.example.shop.persistence.entities;

import com.example.shop.persistence.entities.enums.ProductCategory;
import com.example.shop.persistence.entities.utils.PersistableEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "Класс описывающий сущность нашего продукта.")
public class Product extends PersistableEntity {

    @ApiModelProperty(required = true, value = "Название продукта")
    private String title;
    private String description;
    private Date added;
    private Double price;
    private boolean available;

    @Enumerated
    private ProductCategory category;

    @OneToOne
    @JoinColumn(name = "image")
    private Image image;

}