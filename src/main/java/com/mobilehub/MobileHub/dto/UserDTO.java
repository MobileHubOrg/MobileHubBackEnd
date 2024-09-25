package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
   private Long userId;
    private String login;
    private String email;
    private String role;
    private List<OrderDTO> orders;
}
