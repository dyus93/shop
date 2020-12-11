package ru.geekbrains.shop.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shop.persistence.entities.Review;
import com.example.shop.services.ProductService;
import com.example.shop.services.ReviewService;
import com.example.shop.services.ShopuserService;
import com.example.shop.services.feign.clients.ShopFeignClient;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AmqpTemplate amqpTemplate;

    @MockBean
    private ProductService productService;

    @MockBean
    private ReviewService reviewServiceMock;

    @MockBean
    private ShopFeignClient shopFeignClientMock;

    @MockBean
    private ShopuserService shopuserServiceMock;

    @Before
    public void setUp() {
        List<Review> reviews = new ArrayList<>() {{
            add(new Review());
            add(new Review());
            add(new Review());
            add(new Review());
        }};

        given(reviewServiceMock.getAll()).willReturn(reviews);
    }

    @Test
    public void testMustReturnAllAvailableReviews() throws Exception {
        mockMvc
            .perform(get("/reviews/all")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)));
    }

}
