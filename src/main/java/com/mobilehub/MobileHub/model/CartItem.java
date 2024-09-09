package com.mobilehub.MobileHub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id")  // Связь по cart_id
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id")  // Связь по product_id
    private Product product;
    private Integer quantity;
    private Double price;
    public CartItem(Cart cart, Product product, Integer quantity, Double price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
