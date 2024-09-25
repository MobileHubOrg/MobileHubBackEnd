package com.mobilehub.MobileHub.service;

import com.mobilehub.MobileHub.dto.UserDTO;
import com.mobilehub.MobileHub.dto.UserEditDTO;
import com.mobilehub.MobileHub.dto.UserProfileDTO;
import com.mobilehub.MobileHub.dto.UserRegisterDTO;
import com.mobilehub.MobileHub.exeption.InvalidEmailException;
import com.mobilehub.MobileHub.model.User;
import  com.mobilehub.MobileHub.exeption.UserNotFoundException;

import com.mobilehub.MobileHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.classfile.ClassFormatException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
   final ModelMapper modelMapper ;
   final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
//    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_.+-]+@gmail\\.com$");
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.error("Email is null or empty");
            throw new InvalidEmailException("Email cannot be null or empty");
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            logger.error("Email '{}' does not match the required format", email);
            throw new InvalidEmailException("Invalid email format. Expected format: example@domain.com");
        }
    }
        public UserDTO register (UserRegisterDTO userRegisterDTO){
            if (userRegisterDTO.getEmail() != null) {
                try {
                    validateEmail(userRegisterDTO.getEmail());
                } catch (ClassFormatException e) {

                    logger.error("Invalid email format: {}", userRegisterDTO.getEmail());
                    throw new InvalidEmailException("Invalid email format");
                }
            }
            if (userRepository.existsByLogin(userRegisterDTO.getLogin())) {
                logger.error("User with login {} already exists", userRegisterDTO.getLogin());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this login already exists");
            }
            String password = passwordEncoder.encode(userRegisterDTO.getPassword());
            User user=modelMapper.map(userRegisterDTO,User.class);
            user.setLogin(userRegisterDTO.getLogin());
            user.setRole("USER");
            user.setEmail((userRegisterDTO.getEmail()));
           user.setPassword(password);

            userRepository.save(user);
            logger.info("User {} has been successfully registered", userRegisterDTO.getLogin());
            return modelMapper.map(user, UserDTO.class);
        }
        public String authenticateUser (String username, String password){
            // Assume userService is a service that handles user-related operations
//        User user = userService.findByUsername(username);
//
//        if (user == null) {
//            return "Error: User not found";
//        }
//
//        // Assume passwordEncoder is a bean that handles password encryption
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            return "Error: Invalid password";
//        }
//
//        // Assume jwtTokenProvider is a service that generates JWT tokens
//        String token = jwtTokenProvider.createToken(username, user.getRole());

//        return token;
            return null;
        }
    public UserProfileDTO getUserProfile(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login '" + login + "' not found"));
        return modelMapper.map(user, UserProfileDTO.class);
    }

    public UserDTO updateUser(String login, UserEditDTO userEditDTO) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login '" + userEditDTO.getLogin() + "' not found"));


        String password = passwordEncoder.encode(userEditDTO.getPassword());

        user.setLogin(userEditDTO.getLogin());
        user.setRole(userEditDTO.getRole());
        user.setEmail(userEditDTO.getEmail());
        user.setPassword(password);

        userRepository.save(user);


        return modelMapper.map(user, UserDTO.class);
    }
    public void changePassword(String login, String newPassword) {
        User user = userRepository.findByLogin(login.trim())
                .orElseThrow(() -> new UserNotFoundException("User with login '" + login + "' not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
        @Transactional
        public UserDTO removeUser (String login){

            User user = userRepository.findByLogin(login.trim())

                    .orElseThrow(() -> new UserNotFoundException("User with login '" + login + "' not found"));

            userRepository.deleteByLogin(login);
        return modelMapper.map(user, UserDTO.class);
        }

    }


