package com.mobilehub.MobileHub.repository;

import com.mobilehub.MobileHub.model.Orders;
import com.mobilehub.MobileHub.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    Optional<Orders> findOrderByOrderId(@NonNull Long id);
}
