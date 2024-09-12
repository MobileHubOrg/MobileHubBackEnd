package com.mobilehub.MobileHub.config;
import com.mobilehub.MobileHub.dto.CartDTO;
import com.mobilehub.MobileHub.dto.CartItemDTO;
import com.mobilehub.MobileHub.model.Cart;
import com.mobilehub.MobileHub.model.CartItem;
import com.mobilehub.MobileHub.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class AppConfig {

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(Cart.class, CartDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getLogin(), CartDTO::setLogin);
        });

        // Маппинг Product -> CartItemDTO
        modelMapper.typeMap(CartItem.class, CartItemDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getProduct().getProductName(), CartItemDTO::setProductName);
        });
        return modelMapper;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
