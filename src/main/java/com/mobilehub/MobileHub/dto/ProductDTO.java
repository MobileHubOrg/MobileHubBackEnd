package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {

    private String productName;
    private String description;
    private Double price;
    private String brand;
    private String category;
    private Integer stockQuantity;
    private String imageUrl; // Поле для URL изображения
    private String productUrl; // Поле для ссылки на страницу продукта
    private String releaseDate;

}
