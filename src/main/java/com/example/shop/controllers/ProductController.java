package com.example.shop.controllers;

import com.example.shop.persistence.entities.Review;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.dto.ProductDto;
import com.example.shop.persistence.entities.Image;
import com.example.shop.persistence.entities.Product;
import com.example.shop.services.ImageService;
import com.example.shop.services.ProductService;
import com.example.shop.services.ReviewService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
@Api("Набор методов для витрины онлайн-магазина")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ReviewService reviewService;

    @ApiOperation(value = "Получить продукт в одном количестве", response = String.class)
    @GetMapping("/{id}")
    public String getOne(Model model, @PathVariable String id) {

        Optional<Product> productOptional = productService.getOneById(UUID.fromString(id));

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Review> reviews = reviewService.getReviewsByProduct(product);
            model.addAttribute("product", product);
            model.addAttribute("reviews", reviews);
        }

        return "product";
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String id) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageService.loadFileAsResource(id);
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage,"png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return new byte[0];
        }
    }

    @PostMapping
    public String addOne(@RequestParam("image") MultipartFile image, ProductDto productDto) throws IOException {
        Image img = imageService.uploadImage(image, productDto.getTitle());
        return productService.save(productDto, img);
    }

//    @RabbitListener(queues = "super-shop.queue")
//    public void listenTo(Message message) {
//        try {
//            String reqMessage = new String(message.getBody(), StandardCharsets.UTF_8);
//            System.out.println(reqMessage);
//        } catch (Throwable th) {
//            log.error("Fatal error: can't process Generated Report response", th);
//        }
//    }

}