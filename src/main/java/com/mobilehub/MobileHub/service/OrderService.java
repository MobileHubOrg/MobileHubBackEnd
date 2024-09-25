package com.mobilehub.MobileHub.service;

import com.mobilehub.MobileHub.dto.*;
import com.mobilehub.MobileHub.exeption.OrderNotFoundException;
import com.mobilehub.MobileHub.exeption.ProductNotFoundException;
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


    public OrderDTO createOrder(String login) {
             User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

               Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

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
        Double total = calculateOrderTotal(order.getOrderId());
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

    public List<OrderDTO> getOrdersByUser(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getOrders().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean deleteOrder(String login, Long orderId) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Order does not belong to user");
        }

        orderRepository.delete(order);
        return true;
    }

    public Double  calculateOrderTotal(Long orderId){
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        Double total = order.getOrderItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        return total;
    }
    public ProductDTO addProductToOrder(Long orderId, Long productId, Integer quantity) {
        // Найти заказ по ID
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        // Найти продукт по ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Проверить, есть ли достаточно товара на складе
//        if (product.getStockQuantity() < quantity) {
//            throw new InsufficientStockException("Not enough stock available");
//        }

        // Создать новый элемент заказа (OrderItem)
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order); // Связываем с заказом
        orderItem.setProduct(product); // Связываем с продуктом
        orderItem.setQuantity(quantity); // Указываем количество
        orderItem.setPrice(product.getPrice() * quantity); // Рассчитываем общую цену за этот продукт

        // Добавляем новый элемент заказа в список orderItems заказа
        order.getOrderItems().add(orderItem);

        // Обновляем общую стоимость заказа
        double newTotal = order.getTotal() + orderItem.getPrice();
        order.setTotal(newTotal);

        // Сохраняем изменения в базе данных
        orderRepository.save(order);

        // Уменьшаем количество товара на складе
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // Возвращаем DTO продукта
        return modelMapper.map(product,ProductDTO.class);
    }

    public ProductDTO removeProductFromOrder(Long orderId, String productName) {
        // Находим заказ по его ID
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        // Ищем элемент заказа (OrderItem) по имени продукта
        OrderItem orderItemToRemove = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getProduct().getProductName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in order"));

        // Уменьшаем общую стоимость заказа на сумму стоимости удаляемого продукта
        double newTotal = order.getTotal() - orderItemToRemove.getPrice();
        order.setTotal(newTotal);

        // Удаляем элемент заказа (OrderItem) из списка orderItems заказа
        order.getOrderItems().remove(orderItemToRemove);

        // Возвращаем товар обратно на склад
        Product product = orderItemToRemove.getProduct();
        product.setStockQuantity(product.getStockQuantity() + orderItemToRemove.getQuantity());
        productRepository.save(product);

        // Сохраняем изменения в заказе
        orderRepository.save(order);

        // Возвращаем DTO удалённого продукта
        return modelMapper.map(product,ProductDTO.class);
    }



    public Double calculateOrderTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();
    }


    }