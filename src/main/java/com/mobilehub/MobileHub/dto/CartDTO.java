package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartDTO {
    private Long cartId;
    private Long userId;
    private List<CartItemDTO> cartItems;
}
