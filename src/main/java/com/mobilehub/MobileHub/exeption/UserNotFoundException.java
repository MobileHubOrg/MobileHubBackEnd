//package com.mobilehub.MobileHub.exeption;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.io.Serial;
//
//@ResponseStatus(HttpStatus.NOT_FOUND)
//public class UserNotFoundException extends RuntimeException {
//    @Serial
//    private static final long serialVersionUID = 2755166764452062685L;
//}

package com.mobilehub.MobileHub.exeption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2755166764452062685L;

    // Добавляем логгер
    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    // Конструктор по умолчанию
    public UserNotFoundException() {
        super("User not found");
        logger.error("User not found");
    }

    // Конструктор с параметром - сообщение
    public UserNotFoundException(String message) {
        super(message);
        logger.error("User not found: {}", message);
    }

    // Конструктор с параметром - логин пользователя
    public UserNotFoundException(String message, String login) {
        super(message);
        logger.error("User with login '{}' not found: {}", login, message);
    }

    // Конструктор с параметром - причина
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.error("User not found due to {}: {}", cause.getMessage(), message, cause);
    }
}
