package com.address_verification.addressVerificationApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {

    @NotBlank()
    private String country;
    @NotBlank()
    private String state;
    @NotBlank()
    private String city;
    @NotNull()
    private  String formattedAddress;
}
