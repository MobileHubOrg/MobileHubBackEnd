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
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        boolean added = cartService.addToCart(userId, productId, quantity);
        if (added) {
            return ResponseEntity.ok("Item added to cart successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to add item to cart");
        }
    }
    @DeleteMapping("/delete")
    public Boolean removeFromCart(@RequestParam Long userId,@RequestParam Long productId){
        return  cartService.removeFromCart(userId,productId);
    }
    @GetMapping("/get/{userId}")
    public CartDTO getCart(@PathVariable Long userId){
        return  cartService.getCart(userId);
    }
    @PutMapping("/update")
    public CartItemDTO updateCartItem(@RequestParam  Long userId,
                                      @RequestParam  Long productId,
                                      @RequestParam int quantity,
                                      @RequestParam Double price){

        return  cartService.updateCartItem(userId,productId,quantity,price);
    }
}
