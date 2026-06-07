package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import org.springframework.http.ResponseEntity;

public interface IVerifyAddressService {

    public ApiRespondsData<AddressDTO> verifyUserAddress(GeolocationDTO geolocation);

}
