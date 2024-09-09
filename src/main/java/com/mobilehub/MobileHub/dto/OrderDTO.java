package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemDTO> orderItems;
}
