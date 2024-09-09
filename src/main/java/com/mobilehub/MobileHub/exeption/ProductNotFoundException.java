package com.mobilehub.MobileHub.exeption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;



@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ProductNotFoundException.class);
    @Serial
    private static final long serialVersionUID = 2755166764452062685L;
    public ProductNotFoundException(String message) {
        super(message);
        logger.error("Product not found: {}", message);
    }
}
