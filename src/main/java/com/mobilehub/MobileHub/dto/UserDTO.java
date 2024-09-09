package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
   private Long userId;
    private String login;
    private String email;
    private String role;
}
