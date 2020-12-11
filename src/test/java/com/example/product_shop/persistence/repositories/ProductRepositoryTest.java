package ru.geekbrains.shop.persistence.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.shop.persistence.entities.Product;
import com.example.shop.persistence.entities.enums.ProductCategory;

import java.io.IOException;
import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    private List<Product> productMocks;

    private ObjectMapper objectMapper = new ObjectMapper();

    {
        try {
            productMocks = objectMapper.readValue(new ClassPathResource("mocks/products.json").getFile(), new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        productMocks.forEach(
            product -> testEntityManager.persist(product)
        );
    }

    @Test
    public void mustFindAllDrinks() {
        Assert.assertEquals(1, productRepository.findAllByCategory(ProductCategory.DRINK).size());
    }

    @Test
    public void mustFindAllFood() {
        Assert.assertEquals(3, productRepository.findAllByCategory(ProductCategory.FOOD).size());
    }

    @Test
    public void mustFindNoProducts() {
        Assert.assertEquals(0, productRepository.findAllByCategory(null).size());
    }

    @Test
    public void mustAllProducts() {
        Assert.assertEquals(4, productRepository.findAll().size());
    }

}