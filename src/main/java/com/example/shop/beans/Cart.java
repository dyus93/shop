package com.example.shop.beans;

import com.example.shop.persistence.entities.CartRecord;
import com.example.shop.persistence.entities.Product;
import lombok.Data;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ru.geekbrains.paymentservice.Payment;
import com.example.shop.services.soap.PaymentService;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private List<CartRecord> cartRecords;
    private List<Payment> payments;
    private Double price;

    private final PaymentService paymentService;

    @PostConstruct
    public void init() {
        cartRecords = new ArrayList<>();
        payments = paymentService.getPayments("Russia");
    }

    public void clear() {
        cartRecords.clear();
        recalculatePrice();
    }

    public void add(Product product) {
        for (CartRecord cartRecord : cartRecords) {
            if (cartRecord.getProduct().getId().equals(product.getId())) {
                cartRecord.setQuantity(cartRecord.getQuantity() + 1);
                cartRecord.setPrice(cartRecord.getQuantity() * cartRecord.getProduct().getPrice());
                recalculatePrice();
                return;
            }
        }
        cartRecords.add(new CartRecord(product));
        recalculatePrice();
    }

    public void removeByProductId(UUID productId) {
        for (int i = 0; i < cartRecords.size(); i++) {
            if (cartRecords.get(i).getProduct().getId().equals(productId)) {
                cartRecords.remove(i);
                recalculatePrice();
                return;
            }
        }
    }

    private void recalculatePrice() {
        price = 0.0;
        cartRecords.forEach(cartRecord -> price = price + cartRecord.getPrice());
    }

}