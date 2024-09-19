package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class OrderSummaryDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private String status;
    private Double total;
}
