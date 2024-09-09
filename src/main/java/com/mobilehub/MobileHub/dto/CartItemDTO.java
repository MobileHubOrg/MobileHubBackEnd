package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
