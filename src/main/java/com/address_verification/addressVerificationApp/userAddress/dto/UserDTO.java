package com.address_verification.addressVerificationApp.userAddress.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Full Name is required")
    @Pattern(
            regexp = "^[a-zA-Z ]+$",
            message = "Full name must contain only letters and spaces"
    )
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Provide a Valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

//    @NotBlank(message = "Longitude is required")
//    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
//    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
//    private Double longitude;
//
//    @NotBlank(message = "Latitude is required")
//    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
//    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
//    private Double latitude;
}
