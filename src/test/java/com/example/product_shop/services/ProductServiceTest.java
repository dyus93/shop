package ru.geekbrains.shop.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.shop.dto.ProductDto;
import com.example.shop.persistence.entities.enums.ProductCategory;
import com.example.shop.services.feign.clients.ShopFeignClient;
import com.example.shop.services.notification.MailService;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private MailService mailService;

    @MockBean
    private ShopFeignClient shopFeignClient;

    @Before
    public void setUp() {
        ProductDto productDto = ProductDto.builder()
            .title("Кофе")
            .available(true)
            .category(ProductCategory.DRINK)
            .price(200.00)
            .description("")
        .build();

        productService.save(productDto, null);
    }

    @Test
    public void testSomething() {
        assertEquals(1, productService.getAll(0).size());
    }

}