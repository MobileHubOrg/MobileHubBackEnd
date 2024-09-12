package com.mobilehub.MobileHub.controller;

import com.mobilehub.MobileHub.dto.CartDTO;
import com.mobilehub.MobileHub.dto.CartItemDTO;
import com.mobilehub.MobileHub.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/cart")
@RestController
public class CartController {
    private  final CartService cartService;
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam String login,
            @RequestParam String productName,
            @RequestParam int quantity
    ) {
        boolean added = cartService.addToCart(login, productName, quantity);
        if (added) {
            return ResponseEntity.ok("Item added to cart successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to add item to cart");
        }
    }
    @DeleteMapping("/delete")
    public Boolean removeFromCart(@RequestParam String login,
                                  @RequestParam String productName){
        return  cartService.removeFromCart(login,productName);
    }
    @GetMapping("/get/{login}")
    public CartDTO getCart(@PathVariable String login){
        return  cartService.getCart(login);
    }
    @PutMapping("/update")
    public CartItemDTO updateCartItem(@RequestParam  String login,
                                      @RequestParam  String productName,
                                      @RequestParam int quantity,
                                      @RequestParam Double price){
        System.out.println("in updateCatItem");
        return  cartService.updateCartItem(login,productName,quantity,price);
    }
}
