package com.mobilehub.MobileHub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileDTO {
    private Long userId;
    private String login;
    private String email;
    private List<OrderSummaryDTO> orderHistory; // List of order summaries
}
