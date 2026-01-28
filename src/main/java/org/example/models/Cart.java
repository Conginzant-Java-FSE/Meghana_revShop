package org.example.models;

import java.time.LocalDateTime;

public class Cart {

    private int cartId;
    private int buyerId;
    private LocalDateTime createdAt;

    public Cart() {}

    public Cart(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
