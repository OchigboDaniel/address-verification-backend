package com.address_verification.addressVerificationApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GeolocationDTO {
    @NotNull( message = "Latitude is Required" )
    double latitude;
    @NotNull( message = "Longitude is Required" )
    double longitude;
}
