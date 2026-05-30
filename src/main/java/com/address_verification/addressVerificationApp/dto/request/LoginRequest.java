package com.address_verification.addressVerificationApp.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email is necessary")
    @Email(message = "valid email")
    private String email;

    @NotBlank(message = "Password is necessary")
    private String password;
}
