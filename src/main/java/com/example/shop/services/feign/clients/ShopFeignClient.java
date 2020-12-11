package com.example.shop.services.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ShopFeignClient", url = "${documents.service.url}")
public interface ShopFeignClient {

    @GetMapping(value = "/flyers")
    ResponseEntity<byte[]> getFlyer();

}