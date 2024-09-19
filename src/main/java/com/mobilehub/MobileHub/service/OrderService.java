package com.mobilehub.MobileHub.service;

import com.mobilehub.MobileHub.dto.*;
import com.mobilehub.MobileHub.exeption.CartNotFoundException;
import com.mobilehub.MobileHub.exeption.OrderNotFoundException;
import com.mobilehub.MobileHub.exeption.UserNotFoundException;
import com.mobilehub.MobileHub.model.*;
import com.mobilehub.MobileHub.repository.CartRepository;
import com.mobilehub.MobileHub.repository.OrderRepository;
import com.mobilehub.MobileHub.repository.ProductRepository;
import com.mobilehub.MobileHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

private  final OrderRepository orderRepository;
    private  final ModelMapper modelMapper;
private final UserRepository userRepository;
private  final  CartRepository cartRepository;
private  final ProductRepository productRepository;

//  number 2
    public OrderDTO createOrder(String login) {
        // Находим пользователя по логину
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Находим корзину пользователя
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Проверка на пустую корзину
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Создаем один заказ
        Orders order = new Orders();
        order.setUser(user);  // Привязываем пользователя к заказу
        order.setOrderDate(LocalDate.now());  // Устанавливаем текущую дату
        order.setStatus("New");

        // Преобразуем товары из корзины в объекты OrderItem
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

                    // Создаем элемент заказа
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);  // Привязываем к заказу
                    orderItem.setProduct(product);
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());

                    return orderItem;
                })
                .collect(Collectors.toList());

        // Устанавливаем товары в заказ
        order.setOrderItems(orderItems);

        // Рассчитываем общую стоимость заказа
        Double total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotal(total);  // Устанавливаем общую стоимость

        // Сохраняем заказ в базу данных
        orderRepository.save(order);

        // Очищаем корзину (если требуется)
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // Преобразуем заказ в DTO для возвращения клиенту
        OrderDTO orderDTO = new OrderDTO();
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setProductId(orderItem.getProduct().getProductId());
                    orderItemDTO.setProductName(orderItem.getProduct().getProductName());
                    orderItemDTO.setProductDescription(orderItem.getProduct().getDescription());
                    orderItemDTO.setProductImage(orderItem.getProduct().getImageUrl());
                    orderItemDTO.setQuantity(orderItem.getQuantity());
                    orderItemDTO.setPrice(orderItem.getPrice());
                    return orderItemDTO;
                })
                .collect(Collectors.toList());

        // Устанавливаем значения в DTO
        orderDTO.setOrderItems(orderItemDTOs);
        orderDTO.setTotal(total);
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus());

        return orderDTO;
    }






    public  OrderDetailsDto getOrderDetails(String login){
        Orders order=orderRepository.findOrdersByUser_Login(login).orElseThrow(()->new OrderNotFoundException("Order with login -> " + login + " not found"));
        System.out.println( );
        return modelMapper.map(order, OrderDetailsDto.class);
    }

    public OrderDetailsDto updateOrderStatus(Long orderId, String newStatus) {
        // Получаем заказ из базы данных по orderId
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));

        // Проверяем, можно ли обновить статус заказа
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        // Обновляем статус заказа
        order.setStatus(newStatus);

        // Сохраняем изменения
        orderRepository.save(order);

        // Возвращаем обновленный объект заказа в виде DTO
        return modelMapper.map(order, OrderDetailsDto.class);
    }

    // Пример метода для проверки допустимых переходов статуса
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "New":
                return newStatus.equals("Processing") || newStatus.equals("Cancelled");
            case "Processing":
                return newStatus.equals("Shipped") || newStatus.equals("Cancelled");
            case "Shipped":
                return newStatus.equals("Delivered");
            case "Delivered":
                return false; // Статус "Доставлен" окончательный, его нельзя изменить
            case "Cancelled":
                return false; // Статус "Отменен" окончательный, его нельзя изменить
            default:
                return false; // Неверный статус
        }
    }

    public  OrderDetailsDto getOrdersByUser(String login){

             return null;
    }
    @Transactional
    public Boolean deleteOrder (String login) {
             return  null;
    }
    public Double  calculateOrderTotal(String login){
             return null;
    }
    public ProductDTO addProductToOrder(ProductDTO productDTO){
             return  null;
         }
    public ProductDTO removeProductFromOrder(ProductDTO productDTO){
             return  null;
         }

    public Double calculateOrderTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();
    }


    }