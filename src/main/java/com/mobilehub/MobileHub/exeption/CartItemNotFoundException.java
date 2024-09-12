package com.mobilehub.MobileHub.exeption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;


    public class CartItemNotFoundException extends  RuntimeException{
private static final Logger logger=LoggerFactory.getLogger(CartItemNotFoundException.class);
@Serial
private static final long serialVersionUID=2755166764452062685L;
public CartItemNotFoundException(String message){
        super(message);
        logger.error("Cart not found: {}",message);
        }
        }
