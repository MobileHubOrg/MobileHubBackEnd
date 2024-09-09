package com.mobilehub.MobileHub.controller;

import com.mobilehub.MobileHub.dto.UserDTO;
import com.mobilehub.MobileHub.dto.UserEditDTO;
import com.mobilehub.MobileHub.dto.UserProfileDTO;
import com.mobilehub.MobileHub.dto.UserRegisterDTO;
import com.mobilehub.MobileHub.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private  final UserService userService;
    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegisterDTO userRegisterDTO) {
        System.out.println(userRegisterDTO.getLogin());
        return userService.register(userRegisterDTO);
    }
    @GetMapping("/{userId}")
    public UserProfileDTO getUser(@PathVariable Long userId) {
               return userService.getUserProfile(userId);
    }
    @PutMapping("/update/{userId}")
    public UserDTO updateUser(@PathVariable Long userId, @RequestBody UserEditDTO userEditDTO) {
        return userService.updateUser(userId, userEditDTO);
    }

    @DeleteMapping("/removeUser/{login}")
    public UserDTO removeUser(@PathVariable String login) {
        System.out.println("in delete");
        return userService.removeUser(login);
    }
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@NonNull Principal principal, @RequestHeader("X-Password") String newPassword) {
        userService.changePassword(principal.getName(), newPassword);
    }
}
