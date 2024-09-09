package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
    public class ProductSummaryDTO {
        private Long productId;
        private String productName;
        private Double price;
        private String brand;
        private String category;

    }
