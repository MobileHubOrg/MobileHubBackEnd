package com.mobilehub.MobileHub.controller;

import com.mobilehub.MobileHub.dto.*;
import com.mobilehub.MobileHub.service.OrderService;
import com.mobilehub.MobileHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Boolean deleteOrder(@RequestParam String login,
                            @RequestParam Long orderId   ){
        return  orderService.deleteOrder(login,orderId);
    }
    @GetMapping("/calculateOrder/{login}")
    public Double calculateOrderTotal(@PathVariable Long orderId){
        return  orderService.calculateOrderTotal(orderId);
    }
    @GetMapping("/getOrder/{login}")
    public List<OrderDTO> getOrdersByUser(@PathVariable String login){
        return  orderService.getOrdersByUser(login);
    }
    @PutMapping("/updateStatus")
    public  OrderDetailsDto updateOrderStatus(@RequestParam Long orderId,
                                              @RequestParam String newStatus){

        return orderService.updateOrderStatus(orderId,newStatus);
    }
    @PostMapping ("/addProduct")
    public ProductDTO addProductToOrder( @RequestParam Long orderId,
                                         @RequestParam Long productId,
                                         @RequestParam Integer quantity){
        return  orderService.addProductToOrder(orderId, productId, quantity);
    }
    @DeleteMapping("/removeProduct")
    public ProductDTO removeProductFromOrder(@RequestParam Long  orderId,
                                             @RequestParam String productName){
        return  orderService.removeProductFromOrder(orderId, productName);
    }

}
