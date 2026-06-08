package com.address_verification.addressVerificationApp.dto;

import com.address_verification.addressVerificationApp.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Role role;
}
