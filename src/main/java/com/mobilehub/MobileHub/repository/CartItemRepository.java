package com.mobilehub.MobileHub.repository;


import com.mobilehub.MobileHub.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Метод для поиска всех карточек в корзине по ID пользователя
    List<CartItem> findByCart_User_UserId(Long userId);
}
