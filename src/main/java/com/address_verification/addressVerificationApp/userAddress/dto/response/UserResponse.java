package com.address_verification.addressVerificationApp.userAddress.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
}
