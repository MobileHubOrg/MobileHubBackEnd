package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long cartItemId;
    private String productName;
    private Integer quantity;
    private Double price;
}
