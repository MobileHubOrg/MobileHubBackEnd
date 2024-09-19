package com.mobilehub.MobileHub.repository;

import com.mobilehub.MobileHub.model.Cart;
import com.mobilehub.MobileHub.model.Orders;
import com.mobilehub.MobileHub.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    Optional<Orders> findOrderByOrderId(@NonNull Long id);
    Optional<Orders>  findOrdersByUser_Login(String login);

    Optional<Orders> findTopByUser_UserIdOrderByOrderDateDesc(Long userId);
}
