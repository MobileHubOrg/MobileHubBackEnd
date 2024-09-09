package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
