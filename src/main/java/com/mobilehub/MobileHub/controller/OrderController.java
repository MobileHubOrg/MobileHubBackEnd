package com.mobilehub.MobileHub.controller;

import com.mobilehub.MobileHub.dto.CartDTO;
import com.mobilehub.MobileHub.dto.CartItemDTO;
import com.mobilehub.MobileHub.dto.OrderDTO;
import com.mobilehub.MobileHub.dto.OrderDetailsDto;
import com.mobilehub.MobileHub.service.OrderService;
import com.mobilehub.MobileHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private  final OrderService orderService;
    @PostMapping("/create")
    public OrderDTO createOrder( @RequestParam Long userId){
        return orderService.createOrder(userId);
    }



    @GetMapping("/getOrder")
    public OrderDetailsDto getOrderDetails( @RequestParam  Long orderId){
        return orderService.getOrderDetails(orderId);
    }
}
