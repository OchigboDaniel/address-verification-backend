package com.address_verification.addressVerificationApp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private double latitude;
    private double longitude;
    private String state;
    private String country;
    private String formattedAddress;
}
