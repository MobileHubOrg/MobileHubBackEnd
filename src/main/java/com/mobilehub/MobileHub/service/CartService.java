package com.mobilehub.MobileHub.service;

import com.mobilehub.MobileHub.dto.CartDTO;
import com.mobilehub.MobileHub.dto.CartItemDTO;
import com.mobilehub.MobileHub.exeption.CartItemNotFoundException;
import com.mobilehub.MobileHub.exeption.CartNotFoundException;
import com.mobilehub.MobileHub.exeption.ProductNotFoundException;
import com.mobilehub.MobileHub.exeption.UserNotFoundException;
import com.mobilehub.MobileHub.model.Cart;
import com.mobilehub.MobileHub.model.CartItem;
import com.mobilehub.MobileHub.model.Product;
import com.mobilehub.MobileHub.model.User;
import com.mobilehub.MobileHub.repository.CartRepository;
import com.mobilehub.MobileHub.repository.ProductRepository;
import com.mobilehub.MobileHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private  final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private  final ModelMapper modelMapper;
@Transactional
    public Boolean addToCart(String login, String productName, int quantity) {
        try {
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new UserNotFoundException("User with login '" + login + "' not found"));
            Cart cart = cartRepository.findCartByUser_Login(login)
                    .orElseGet(() -> {
                        Cart newCart = createNewCart(user.getUserId());
                        if (newCart == null) {
                            throw new RuntimeException("Failed to create new cart for user: " + login);
                        }
                        return newCart;
                    });

        Product product=productRepository.findProductByProductName(productName).orElseThrow(()->new ProductNotFoundException("Poduct with productId '" + productName+ "' not found"));

        List<CartItem>cartItems=cart.getCartItems();
//        CartItem existingCartItem=cartItems.stream()
//                .filter(item->item.getProduct().getProductId().equals(productId))
//                .findFirst().orElse(null);
//        if (existingCartItem!=null){
//            existingCartItem.setQuantity(existingCartItem.getQuantity()+quantity);
//        }else {
//            CartItem newCartItem=new CartItem(cart,product,quantity,product.getPrice());
//             cartItems.add(newCartItem);
//        }
//        cartRepository.save(cart);
//        return  true;
//
//    } catch (UserNotFoundException | ProductNotFoundException e) {
//        // Логирование ошибки
//        // Можно также выбросить новое исключение или вернуть false
//        return false;
//    }

            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                    .findFirst()
                    .orElseGet(() -> {
                        CartItem newItem = new CartItem(cart, product, 0, product.getPrice());
                        cart.getCartItems().add(newItem);
                        return newItem;
                    });

            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            cartRepository.save(cart);
            return true;
        } catch (Exception e) {
            // Логирование ошибки
//            log.error("Error adding item to cart: ", e);
            return false;
        }
}

    private Cart createNewCart(Long userId) {
        Cart cart=new Cart();
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
//        User user =userRepository.findUserByUserId(userId).orElseThrow();
//        cart.setUser(userRepository.findUserByUserId(userId).orElseThrow(()->new UserNotFoundException("User not found"+user.getLogin())));
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());

        return  cartRepository.save(cart);
    }

    public Boolean removeFromCart(String login, String productName){
        Cart cart = cartRepository.findCartByUser_Login(login)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        Product product = productRepository.findProductByProductName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product with name '" + productName + "' not found"));
        cart.getCartItems().removeIf(item->item.getProduct().getProductId().equals(product.getProductId()));

        cartRepository.save(cart);
        return  true;
    }

    public CartDTO getCart(String login){
        Cart cart = cartRepository.findCartByUser_Login(login)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        return  modelMapper.map(cart, CartDTO.class);
    }

    public CartItemDTO updateCartItem(String login, String productName, int quantity, Double price){
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login '" + login + "' not found"));
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + login));

        Product product = productRepository.findProductByProductName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product with name '" + productName + "' not found"));

CartItem cartItem=cart.getCartItems().stream()
        .filter(item->item.getProduct().getProductId().equals(product.getProductId()))
        .findFirst().orElseThrow(()->new CartItemNotFoundException("Cart item not found for product: " + productName));
cartItem.setQuantity(quantity);
cartItem.setPrice(price);
cartRepository.save(cart);
        return  modelMapper.map(cartItem,CartItemDTO.class);
        }

        }
