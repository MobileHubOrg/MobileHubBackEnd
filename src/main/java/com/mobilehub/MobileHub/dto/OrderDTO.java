package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import java.util.List;

@Setter
@Getter
public class OrderDTO {
//    private Long orderId;
//    private Long userId;
    private LocalDate orderDate;
    private String status;
    private  Double total;
    private List<OrderItemDTO> orderItems;
}
