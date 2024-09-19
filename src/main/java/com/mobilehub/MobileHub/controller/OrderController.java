package com.mobilehub.MobileHub.controller;

import com.mobilehub.MobileHub.dto.*;
import com.mobilehub.MobileHub.service.OrderService;
import com.mobilehub.MobileHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private  final OrderService orderService;
    @PostMapping("/createOrder/{login}")
    public OrderDTO createOrder(@PathVariable String login){
        return orderService.createOrder(login);
    }



    @GetMapping("/getOrder")
    public OrderDetailsDto getOrderDetails( @RequestParam  String login){

        return orderService.getOrderDetails(login);
    }
    @DeleteMapping("/deleteOrder/{login}")
    public Boolean deleteOrder(@PathVariable String login){
        return  orderService.deleteOrder(login);
    }
    @GetMapping("/calculateOrder/{login}")
    public Double calculateOrderTotal(@PathVariable String login){
        return  orderService.calculateOrderTotal(login);
    }
    @GetMapping("/getOrder/{login}")
    public OrderDetailsDto getOrdersByUser(@PathVariable String login){
        return  orderService.getOrdersByUser(login);
    }
    @PutMapping("/updateStatus")
    public  OrderDetailsDto updateOrderStatus(@RequestParam Long orderId,
                                              @RequestParam String newStatus){

        return orderService.updateOrderStatus(orderId,newStatus);
    }
    @PostMapping ("/addProduct")
    public ProductDTO addProductToOrder(@RequestBody ProductDTO productDTO){
        return  orderService.addProductToOrder(productDTO);
    }
    @DeleteMapping("/removeProduct")
    public ProductDTO removeProductFromOrder(@RequestBody ProductDTO productDTO){
        return  orderService.removeProductFromOrder(productDTO);
    }

}
