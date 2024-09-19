package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderDetailsDto {
//    private Long orderId;
    private String status;
    private List<OrderItemDTO> orderItems;
     private Double total;
}
