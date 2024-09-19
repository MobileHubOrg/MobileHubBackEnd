package com.mobilehub.MobileHub.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderItemDTO {
    private Long productId;

    private String productName;  // Название продукта
    private String productDescription;  // Описание продукта
    private String productImage;  // URL изображения продукта (если нужно)
    private Integer quantity;
    private Double price;
}

