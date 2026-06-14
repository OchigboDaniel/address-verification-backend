package com.address_verification.addressVerificationApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    int id;
    double latitude;
    double longitude;
    String country;
    String state;
    String formattedAddress;
    String validationStatus;
    String ownerEmail;
}