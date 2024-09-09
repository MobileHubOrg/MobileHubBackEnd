package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditDTO {
    private String login;
    private String password;
    private String email;
    private String role;
}
