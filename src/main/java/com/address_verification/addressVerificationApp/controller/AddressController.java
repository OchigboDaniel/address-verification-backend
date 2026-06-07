package com.address_verification.addressVerificationApp.controller;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import com.address_verification.addressVerificationApp.service.VerifyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    VerifyAddressService verifyAddressService;


    @PostMapping("/verify-address")
    public ResponseEntity<ApiRespondsData<AddressDTO>> verifyAddress(@RequestBody GeolocationDTO geolocationDTO){
        ApiRespondsData<AddressDTO> result = verifyAddressService.verifyUserAddress(geolocationDTO);
        return ResponseEntity.status(201).body(result);
    }
}
