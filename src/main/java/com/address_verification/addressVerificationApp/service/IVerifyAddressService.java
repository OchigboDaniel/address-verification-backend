package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import com.address_verification.addressVerificationApp.dto.response.AddressResponse;

import java.util.List;


public interface IVerifyAddressService {

    ApiRespondsData<AddressDTO> verifyUserAddress(GeolocationDTO geolocation);

    ApiRespondsData<List<AddressResponse>> getAllUserandAddress();


}
