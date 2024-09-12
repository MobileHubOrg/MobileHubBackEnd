package com.mobilehub.MobileHub.repository;

import com.mobilehub.MobileHub.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository  extends JpaRepository<Cart,Long> {
   Optional<Cart>  findByUser_UserId(Long userId);
   Optional<Cart>  findCartByUser_Login(String login);
}
