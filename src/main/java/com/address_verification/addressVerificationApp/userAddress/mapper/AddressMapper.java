package com.address_verification.addressVerificationApp.userAddress.mapper;

import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.userAddress.model.Address;

public class AddressMapper {
    public static AddressDTO convertToDTO(Address address){
        AddressDTO addressRequestDTO = new AddressDTO();
        addressRequestDTO.setLatitude(address.getLatitude());
        addressRequestDTO.setLongitude(address.getLongitude());
        addressRequestDTO.setState(address.getState());
        addressRequestDTO.setCountry(address.getCountry());
        addressRequestDTO.setFormattedAddress(address.getFormattedAddress());

        return  addressRequestDTO;
    }


    public static Address convertToAddressEntity(AddressDTO addressRequestDTO){
        Address address = new Address();
        address.setLatitude(addressRequestDTO.getLatitude());
        address.setLongitude(addressRequestDTO.getLongitude());
        address.setState(addressRequestDTO.getState());
        address.setCountry(addressRequestDTO.getCountry());
        address.setFormattedAddress(addressRequestDTO.getFormattedAddress());

        return  address;
    }
}
